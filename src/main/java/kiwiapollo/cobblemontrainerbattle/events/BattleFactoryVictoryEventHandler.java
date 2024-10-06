package kiwiapollo.cobblemontrainerbattle.events;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battlefactory.BattleFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public class BattleFactoryVictoryEventHandler implements BattleVictoryEventHandler {
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

        BattleFactory.sessions.get(player.getUuid()).defeatedTrainerCount += 1;
        BattleFactory.sessions.get(player.getUuid()).isTradedPokemon = false;
    }

    private void onDefeat(BattleVictoryEvent battleVictoryEvent) {
        ServerPlayerEntity player = battleVictoryEvent.getBattle().getPlayers().get(0);

        BattleFactory.sessions.get(player.getUuid()).isDefeated = true;
    }
}
