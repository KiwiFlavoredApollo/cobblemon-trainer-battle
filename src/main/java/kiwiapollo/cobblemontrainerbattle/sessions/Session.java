package kiwiapollo.cobblemontrainerbattle.sessions;

import kiwiapollo.cobblemontrainerbattle.exceptions.BattleStartException;

public interface Session {
    void startBattle() throws BattleStartException;

    void onBattleVictory();

    void onBattleDefeat();

    void onSessionStop();
}
