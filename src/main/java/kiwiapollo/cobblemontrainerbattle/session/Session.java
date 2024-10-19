package kiwiapollo.cobblemontrainerbattle.session;

import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;

public interface Session {
    void startBattle() throws BattleStartException;

    void onSessionStop();
}
