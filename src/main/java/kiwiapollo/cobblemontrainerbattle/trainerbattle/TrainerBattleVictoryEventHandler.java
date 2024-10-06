package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleactors.trainer.EntityBackedTrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.events.BattleVictoryEventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.stream.StreamSupport;

public class TrainerBattleVictoryEventHandler implements BattleVictoryEventHandler {
    @Override
    public void onPlayerVictory(BattleVictoryEvent battleVictoryEvent) {
        ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);
        BattleActor playerBattleActor = battleVictoryEvent.getBattle().getActor(player);

        if (battleVictoryEvent.getWinners().contains(playerBattleActor)) {
            onVictory(battleVictoryEvent);

        } else {
            onDefeat(battleVictoryEvent);
        }

        CobblemonTrainerBattle.trainerBattles.remove(player.getUuid());
    }

    private void onVictory(BattleVictoryEvent battleVictoryEvent) {
        ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);
        BattleActor trainerBattleActor = getTrainerBattleActor(battleVictoryEvent);
        String trainerName = trainerBattleActor.getName().getString();
        JsonObject trainerConfiguration = CobblemonTrainerBattle.trainerFiles.get(trainerName).configuration;

        if (!trainerConfiguration.has("onVictory")) {
            return;
        }

        JsonObject onVictory = trainerConfiguration.get("onVictory").getAsJsonObject();

        if (onVictory.has("balance") && onVictory.get("balance").isJsonPrimitive()) {
            addPlayerBalance(onVictory.get("balance"), player);
        }

        if (onVictory.has("commands") && onVictory.get("commands").isJsonArray()) {
            for (JsonElement commandJsonElement : onVictory.get("commands").getAsJsonArray()) {
                executeCommand(commandJsonElement, player);
            }
        }

        if (trainerBattleActor instanceof EntityBackedTrainerBattleActor) {
            ((EntityBackedTrainerBattleActor) trainerBattleActor).getEntity().remove(Entity.RemovalReason.KILLED);
        }
    }

    private BattleActor getTrainerBattleActor(BattleVictoryEvent battleVictoryEvent) {
        return StreamSupport.stream(battleVictoryEvent.getBattle().getActors().spliterator(), false)
                .filter(battleActor -> !(battleActor instanceof PlayerBattleActor))
                .findFirst().get();
    }

    private void onDefeat(BattleVictoryEvent battleVictoryEvent) {
        ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);
        BattleActor trainerBattleActor = getTrainerBattleActor(battleVictoryEvent);
        String trainerName = trainerBattleActor.getName().getString();
        JsonObject trainerConfiguration = CobblemonTrainerBattle.trainerFiles.get(trainerName).configuration;

        if (!trainerConfiguration.has("onDefeat")) {
            return;
        }

        JsonObject onDefeat = trainerConfiguration.get("onDefeat").getAsJsonObject();

        if (onDefeat.has("balance") && onDefeat.get("balance").isJsonPrimitive()) {
            removePlayerBalance(onDefeat.get("balance"), player);
        }

        if (onDefeat.has("commands") && onDefeat.get("commands").isJsonArray()) {
            for (JsonElement commandJsonElement : onDefeat.get("commands").getAsJsonArray()) {
                executeCommand(commandJsonElement, player);
            }
        }

        if (trainerBattleActor instanceof EntityBackedTrainerBattleActor) {
            ((EntityBackedTrainerBattleActor) trainerBattleActor).getEntity().setAiDisabled(false);
        }
    }

    private void executeCommand(JsonElement commandJsonElement, ServerPlayerEntity player) {
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
}
