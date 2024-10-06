package kiwiapollo.cobblemontrainerbattle.events;

import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;

public interface BattleVictoryEventHandler {
    void onPlayerVictory(BattleVictoryEvent battleVictoryEvent);
}
