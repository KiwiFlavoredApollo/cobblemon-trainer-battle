package kiwiapollo.cobblemontrainerbattle.events;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.mojang.brigadier.CommandDispatcher;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.Trainer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.stream.StreamSupport;

public class EntityBackedTrainerBattleVictoryEventHandler implements BattleVictoryEventHandler {
    @Override
    public void onBattleVictory(BattleVictoryEvent battleVictoryEvent) {
        ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);
        BattleActor playerBattleActor = battleVictoryEvent.getBattle().getActor(player);

        if (battleVictoryEvent.getWinners().contains(playerBattleActor)) {
            onVictory(battleVictoryEvent);

        } else {
            onDefeat(battleVictoryEvent);
        }
    }

    private void onVictory(BattleVictoryEvent battleVictoryEvent) {
        ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);
        BattleActor trainerBattleActor = getTrainerBattleActor(battleVictoryEvent);
        String trainerResourcePath = trainerBattleActor.getName().getString();

        Trainer trainer = CobblemonTrainerBattle.trainers.get(Identifier.of(CobblemonTrainerBattle.NAMESPACE, trainerResourcePath));

        CobblemonTrainerBattle.economy.addBalance(player, trainer.onVictory.balance);

        for (String command : trainer.onVictory.commands) {
            executeCommand(command, player);
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
        String trainerResourcePath = trainerBattleActor.getName().getString();

        Trainer trainer = CobblemonTrainerBattle.trainers.get(Identifier.of(CobblemonTrainerBattle.NAMESPACE, trainerResourcePath));

        CobblemonTrainerBattle.economy.removeBalance(player, trainer.onDefeat.balance);

        for (String command : trainer.onDefeat.commands) {
            executeCommand(command, player);
        }
    }

    private void executeCommand(String command, ServerPlayerEntity player) {
        try {
            command = command.replace("%player%", player.getGameProfile().getName());

            MinecraftServer server = player.getCommandSource().getServer();
            CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();

            server.getCommandManager().execute(
                    dispatcher.parse(command, server.getCommandSource()), command);

        } catch (UnsupportedOperationException e) {
            CobblemonTrainerBattle.LOGGER.error(
                    String.format("Error occurred while running command: %s", command));
        }
    }
}
