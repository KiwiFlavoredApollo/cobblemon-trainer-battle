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
import kiwiapollo.cobblemontrainerbattle.battlefactory.BattleFactory;
import kiwiapollo.cobblemontrainerbattle.groupbattle.GroupBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

public class BattleVictoryEventHandler {
    public void run(BattleVictoryEvent battleVictoryEvent) {
        List<UUID> battleIds = CobblemonTrainerBattle.trainerBattles.values().stream().map(PokemonBattle::getBattleId).toList();
        boolean isCobblemonTrainerBattle = battleIds.contains(battleVictoryEvent.getBattle().getBattleId());
        if (!isCobblemonTrainerBattle) return;

        if (isGroupBattle(battleVictoryEvent)) {
            handleGroupBattleVictoryEvent(battleVictoryEvent);
            return;
        }

        if (isBattleFactory(battleVictoryEvent)) {
            handleBattleFactoryVictoryEvent(battleVictoryEvent);
            return;
        }

        handleTrainerBattleVictoryEvent(battleVictoryEvent);
    }

    private boolean isBattleFactory(BattleVictoryEvent battleVictoryEvent) {
        return BattleFactory.SESSIONS.values().stream()
                .map(session -> session.battleUuid)
                .anyMatch(uuid -> uuid == battleVictoryEvent.getBattle().getBattleId());
    }

    private boolean isGroupBattle(BattleVictoryEvent battleVictoryEvent) {
        return GroupBattle.SESSIONS.values().stream()
                .map(session -> session.battleUuid)
                .anyMatch(uuid -> uuid == battleVictoryEvent.getBattle().getBattleId());
    }

    private void handleTrainerBattleVictoryEvent(BattleVictoryEvent battleVictoryEvent) {
        ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);
        BattleActor playerBattleActor = battleVictoryEvent.getBattle().getActor(player);
        String trainerName = StreamSupport.stream(battleVictoryEvent.getBattle().getActors().spliterator(), false)
                .filter(battleActor -> !battleActor.isForPlayer(player))
                .findFirst().get().getName().getString();

        if (battleVictoryEvent.getWinners().contains(playerBattleActor)) {
            CobblemonTrainerBattle.LOGGER.info(String.format(
                    "%s: Victory against %s", player.getGameProfile().getName(), trainerName));
            onVictoryTrainerBattle(battleVictoryEvent);

        } else {
            CobblemonTrainerBattle.LOGGER.info(String.format(
                    "%s: Defeated by %s", player.getGameProfile().getName(), trainerName));
            onDefeatTrainerBattle(battleVictoryEvent);
        }

        CobblemonTrainerBattle.trainerBattles.remove(battleVictoryEvent.getBattle().getBattleId());
    }

    private void onVictoryTrainerBattle(BattleVictoryEvent battleVictoryEvent) {
        ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);
        String trainerName = StreamSupport.stream(battleVictoryEvent.getBattle().getActors().spliterator(), false)
                .filter(battleActor -> !battleActor.isForPlayer(player))
                .findFirst().get().getName().getString();
        JsonObject trainerConfiguration = CobblemonTrainerBattle.trainerFiles.get(trainerName).configuration;
        JsonObject onVictory = trainerConfiguration.get("onVictory").getAsJsonObject();

        if (onVictory.has("balance") && onVictory.get("balance").isJsonPrimitive()) {
            addPlayerBalance(onVictory.get("balance"), player);
        }

        if (onVictory.has("commands") && onVictory.get("commands").isJsonArray()) {
            for (JsonElement commandJsonElement : onVictory.get("commands").getAsJsonArray()) {
                runCommand(commandJsonElement, player);
            }
        }
    }

    private void onDefeatTrainerBattle(BattleVictoryEvent battleVictoryEvent) {
        ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);
        String trainerName = StreamSupport.stream(battleVictoryEvent.getBattle().getActors().spliterator(), false)
                .filter(battleActor -> !battleActor.isForPlayer(player))
                .findFirst().get().getName().getString();
        JsonObject trainerConfiguration = CobblemonTrainerBattle.trainerFiles.get(trainerName).configuration;
        JsonObject onDefeat = trainerConfiguration.get("onDefeat").getAsJsonObject();

        if (onDefeat.has("balance") && onDefeat.get("balance").isJsonPrimitive()) {
            removePlayerBalance(onDefeat.get("balance"), player);
        }

        if (onDefeat.has("commands") && onDefeat.get("commands").isJsonArray()) {
            for (JsonElement commandJsonElement : onDefeat.get("commands").getAsJsonArray()) {
                runCommand(commandJsonElement, player);
            }
        }
    }

    private void runCommand(JsonElement commandJsonElement, ServerPlayerEntity player) {
        try {
            String command = commandJsonElement.getAsString();
            command = command.replace("%player%", player.getGameProfile().getName());

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

    private void handleGroupBattleVictoryEvent(BattleVictoryEvent battleVictoryEvent) {
        ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);
        BattleActor playerBattleActor = battleVictoryEvent.getBattle().getActor(player);

        if (battleVictoryEvent.getWinners().contains(playerBattleActor)) {
            onVictoryGroupBattle(battleVictoryEvent);

        } else {
            onDefeatGroupBattle(battleVictoryEvent);
        }

        CobblemonTrainerBattle.trainerBattles.remove(battleVictoryEvent.getBattle().getBattleId());
    }

    private void onVictoryGroupBattle(BattleVictoryEvent battleVictoryEvent) {
        ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);

        GroupBattle.SESSIONS.get(player.getUuid()).defeatedTrainers
                .add(getDefeatedTrainer(battleVictoryEvent));
    }

    private void onDefeatGroupBattle(BattleVictoryEvent battleVictoryEvent) {
        ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);

        GroupBattle.SESSIONS.get(player.getUuid()).isDefeated = true;
    }

    private void handleBattleFactoryVictoryEvent(BattleVictoryEvent battleVictoryEvent) {
        ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);
        BattleActor playerBattleActor = battleVictoryEvent.getBattle().getActor(player);

        if (battleVictoryEvent.getWinners().contains(playerBattleActor)) {
            onVictoryBattleFactory(battleVictoryEvent);

        } else {
            onDefeatBattleFactory(battleVictoryEvent);
        }

        CobblemonTrainerBattle.trainerBattles.remove(battleVictoryEvent.getBattle().getBattleId());
    }

    private void onVictoryBattleFactory(BattleVictoryEvent battleVictoryEvent) {
        ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);

        BattleFactory.SESSIONS.get(player.getUuid()).defeatedTrainers
                .add(getDefeatedTrainer(battleVictoryEvent));
        BattleFactory.SESSIONS.get(player.getUuid()).isTradedPokemon = false;
    }

    private void onDefeatBattleFactory(BattleVictoryEvent battleVictoryEvent) {
        ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);

        BattleFactory.SESSIONS.get(player.getUuid()).isDefeated = true;
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
