package kiwiapollo.cobblemontrainerbattle.events;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;
import com.cobblemon.mod.common.battles.actor.TrainerBattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battlefrontier.BattleFrontier;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.StreamSupport;

public class BattleVictoryEventHandler {
    public void run(BattleVictoryEvent battleVictoryEvent) {
        handleTrainerBattleVictoryEvent(battleVictoryEvent);
        handleBattleFrontierBattleVictoryEvent(battleVictoryEvent);
    }

    private void handleTrainerBattleVictoryEvent(BattleVictoryEvent battleVictoryEvent) {
        List<UUID> battleIds = CobblemonTrainerBattle.trainerBattles.values().stream().map(PokemonBattle::getBattleId).toList();
        if (!battleIds.contains(battleVictoryEvent.getBattle().getBattleId())) return;

        ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);
        BattleActor playerBattleActor = battleVictoryEvent.getBattle().getActor(player);
        String trainerName = StreamSupport.stream(battleVictoryEvent.getBattle().getActors().spliterator(), false)
                .filter(battleActor -> !battleActor.isForPlayer(player))
                .findFirst().get().getName().getString();

        if (battleVictoryEvent.getWinners().contains(playerBattleActor)) {
            CobblemonTrainerBattle.LOGGER.info(String.format(
                    "%s: Victory against %s", player.getGameProfile().getName(), trainerName));
            handleOnVictoryEvent(battleVictoryEvent);

        } else {
            CobblemonTrainerBattle.LOGGER.info(String.format(
                    "%s: Defeated by %s", player.getGameProfile().getName(), trainerName));
            handleOnDefeatEvent(battleVictoryEvent);
        }

        CobblemonTrainerBattle.trainerBattles.remove(battleVictoryEvent.getBattle());
    }

    private void handleOnDefeatEvent(BattleVictoryEvent battleVictoryEvent) {
        ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);
        String trainerName = StreamSupport.stream(battleVictoryEvent.getBattle().getActors().spliterator(), false)
                .filter(battleActor -> !battleActor.isForPlayer(player))
                .findFirst().get().getName().getString();
        JsonObject trainerConfiguration = CobblemonTrainerBattle.trainerFiles.get(new Identifier(trainerName)).configuration;
        JsonObject onVictory = trainerConfiguration.get("onVictory").getAsJsonObject();

        if (onVictory.has("balance") && onVictory.get("balance").isJsonPrimitive()) {
            removePlayerBalance(onVictory.get("balance"), player);
        }

        if (onVictory.has("commands") && onVictory.get("commands").isJsonArray()) {
            for (JsonElement commandJsonElement : onVictory.get("commands").getAsJsonArray()) {
                runCommand(commandJsonElement, player);
            }
        }
    }

    private void handleOnVictoryEvent(BattleVictoryEvent battleVictoryEvent) {
        ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);
        String trainerName = StreamSupport.stream(battleVictoryEvent.getBattle().getActors().spliterator(), false)
                .filter(battleActor -> !battleActor.isForPlayer(player))
                .findFirst().get().getName().getString();
        JsonObject trainerConfiguration = CobblemonTrainerBattle.trainerFiles.get(new Identifier(trainerName)).configuration;
        JsonObject onVictory = trainerConfiguration.get("onVictory").getAsJsonObject();

        if (onVictory.has("items") && onVictory.get("items").isJsonArray()) {
            for (JsonElement itemJsonElement : onVictory.get("items").getAsJsonArray()) {
                giveItemToPlayer(itemJsonElement, player);
            }
        }

        if (onVictory.has("balance") && onVictory.get("balance").isJsonPrimitive()) {
            addPlayerBalance(onVictory.get("balance"), player);
        }

        if (onVictory.has("commands") && onVictory.get("commands").isJsonArray()) {
            for (JsonElement commandJsonElement : onVictory.get("commands").getAsJsonArray()) {
                runCommand(commandJsonElement, player);
            }
        }
    }

    private void runCommand(JsonElement commandJsonElement, ServerPlayerEntity player) {
        try {
            String command = commandJsonElement.getAsString();

            MinecraftServer server = player.getCommandSource().getServer();
            CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();

            server.getCommandManager().execute(
                    dispatcher.parse(command, server.getCommandSource()), command);

        } catch (UnsupportedOperationException e) {
            CobblemonTrainerBattle.LOGGER.error(
                    String.format("%s: Error occurred while running command", commandJsonElement.getAsString()));
        }
    }

    private void addPlayerBalance(JsonElement balanceJsonElement, ServerPlayerEntity player) {
        try {
            CobblemonTrainerBattle.ECONOMY.addBalance(player, balanceJsonElement.getAsInt());

        } catch (UnsupportedOperationException e) {
            CobblemonTrainerBattle.LOGGER.error(
                    String.format("%s: Error occurred while adding balance to player", player.getGameProfile().getName()));
        }
    }

    private void removePlayerBalance(JsonElement balanceJsonElement, ServerPlayerEntity player) {
        try {
            CobblemonTrainerBattle.ECONOMY.removeBalance(player, balanceJsonElement.getAsInt());

        } catch (UnsupportedOperationException e) {
            CobblemonTrainerBattle.LOGGER.error(
                    String.format("%s: Error occurred while removing balance from player", player.getGameProfile().getName()));
        }
    }

    private void giveItemToPlayer(JsonElement itemJsonElement, ServerPlayerEntity player) {
        try {
            JsonObject itemJsonObject = itemJsonElement.getAsJsonObject();
            Item item = Objects.requireNonNull(
                    Registries.ITEM.get(new Identifier(itemJsonObject.get("item").getAsString())));
            int itemCount = Objects.requireNonNull(itemJsonObject.get("count")).getAsInt();

            if (!player.getInventory().insertStack(new ItemStack(item, itemCount))) {
                player.dropItem(new ItemStack(item, itemCount), false);
            }

        } catch (IllegalStateException | NullPointerException e) {
            CobblemonTrainerBattle.LOGGER.error(
                    String.format("%s: Error occurred while giving item to player", player.getGameProfile().getName()));
            if (itemJsonElement.isJsonObject()) {
                CobblemonTrainerBattle.LOGGER.error(itemJsonElement.toString());
            }
        }
    }

    private void handleBattleFrontierBattleVictoryEvent(BattleVictoryEvent battleVictoryEvent) {
        boolean isBattleFrontierBattle = BattleFrontier.SESSIONS.values().stream()
                .map(session -> session.battleUuid)
                .anyMatch(uuid -> uuid == battleVictoryEvent.getBattle().getBattleId());
        if (!isBattleFrontierBattle) return;

        ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);
        BattleActor playerBattleActor = battleVictoryEvent.getBattle().getActor(player);

        if (battleVictoryEvent.getWinners().contains(playerBattleActor)) {
            handleBattleFrontierPlayerVictoryEvent(battleVictoryEvent);

        } else {
            handleBattleFrontierPlayerDefeatEvent(battleVictoryEvent);
        }
    }

    private void handleBattleFrontierPlayerDefeatEvent(BattleVictoryEvent battleVictoryEvent) {
        ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);

        BattleFrontier.SESSIONS.get(player.getUuid()).isDefeated = true;
    }

    private void handleBattleFrontierPlayerVictoryEvent(BattleVictoryEvent battleVictoryEvent) {
        ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);

        BattleFrontier.SESSIONS.get(player.getUuid()).battleCount += 1;
        BattleFrontier.SESSIONS.get(player.getUuid()).defeatedTrainers
                .add(getDefeatedTrainer(battleVictoryEvent));
        BattleFrontier.SESSIONS.get(player.getUuid()).isTradedPokemon = false;
    }

    private Trainer getDefeatedTrainer(BattleVictoryEvent battleVictoryEvent) {
        BattleActor defeatedTrainerBattleActor =
                StreamSupport.stream(battleVictoryEvent.getBattle().getActors().spliterator(), false)
                        .filter(battleActor -> battleActor instanceof TrainerBattleActor).toList().get(0);

        String defeatedTrainerName = defeatedTrainerBattleActor.getName().toString();

        List<Pokemon> defeatedTrainerPokemons = defeatedTrainerBattleActor.getPokemonList().stream()
                .map(BattlePokemon::getOriginalPokemon).toList();

        return new Trainer(
                defeatedTrainerName,
                defeatedTrainerPokemons
        );
    }
}
