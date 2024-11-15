package kiwiapollo.cobblemontrainerbattle.event;

import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;
import kiwiapollo.cobblemontrainerbattle.advancement.CustomCriteria;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattleStorage;
import kotlin.Unit;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.UUID;

public class BattleVictoryEventHandler {
    public static Unit onBattleVictory(BattleVictoryEvent battleVictoryEvent) {
        // BATTLE_VICTORY event fires even if the player loses

        List<UUID> battleIds = TrainerBattleStorage.getTrainerBattleRegistry().values().stream()
                .map(TrainerBattle::getBattleId).toList();

        if (!battleIds.contains(battleVictoryEvent.getBattle().getBattleId())) {
            return Unit.INSTANCE;
        }

        if (isPlayerVictory(battleVictoryEvent)) {
            onPlayerVictory(battleVictoryEvent);

        } else {
            onPlayerDefeat(battleVictoryEvent);
        }

        return Unit.INSTANCE;
    }

    private static boolean isPlayerVictory(BattleVictoryEvent battleVictoryEvent) {
        ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);
        return battleVictoryEvent.getWinners().stream().anyMatch(battleActor -> battleActor.isForPlayer(player));
    }

    private static void onPlayerVictory(BattleVictoryEvent battleVictoryEvent) {
        ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);

        TrainerBattleStorage.getTrainerBattleRegistry().get(player.getUuid()).onPlayerVictory();
        CustomCriteria.DEFEAT_TRAINER_CRITERION.trigger(player);

        TrainerBattleStorage.getTrainerBattleRegistry().remove(player.getUuid());
    }

    private static void onPlayerDefeat(BattleVictoryEvent battleVictoryEvent) {
        ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);

        TrainerBattleStorage.getTrainerBattleRegistry().get(player.getUuid()).onPlayerDefeat();

        TrainerBattleStorage.getTrainerBattleRegistry().remove(player.getUuid());
    }
}
