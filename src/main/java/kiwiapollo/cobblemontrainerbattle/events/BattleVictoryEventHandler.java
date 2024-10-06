package kiwiapollo.cobblemontrainerbattle.events;

import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;

public interface BattleVictoryEventHandler {
    void onBattleVictory(BattleVictoryEvent battleVictoryEvent);
}
