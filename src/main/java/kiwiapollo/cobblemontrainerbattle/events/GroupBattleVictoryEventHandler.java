package kiwiapollo.cobblemontrainerbattle.events;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.groupbattle.GroupBattle;
import net.minecraft.server.network.ServerPlayerEntity;

public class GroupBattleVictoryEventHandler implements BattleVictoryEventHandler {
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

        GroupBattle.sessions.get(player.getUuid()).defeatedTrainerCount += 1;
    }

    private void onDefeat(BattleVictoryEvent battleVictoryEvent) {
        ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);

        GroupBattle.sessions.get(player.getUuid()).isDefeated = true;
    }
}
