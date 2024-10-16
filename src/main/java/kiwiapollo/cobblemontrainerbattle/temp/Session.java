package kiwiapollo.cobblemontrainerbattle.temp;

import kiwiapollo.cobblemontrainerbattle.trainerbattle.BattleStartException;

import java.util.UUID;

public interface Session {
    void startBattle() throws BattleStartException;

    void onBattleVictory();

    void onBattleDefeat();

    void onSessionStop();
}
