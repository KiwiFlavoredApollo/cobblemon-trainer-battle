package kiwiapollo.cobblemontrainerbattle.resulthandler;

import kiwiapollo.cobblemontrainerbattle.battleparticipant.TrainerBattleParticipant;

public class SessionBattleResultHandler implements ResultHandler {
    private final Runnable onVictory;
    private final Runnable onDefeat;

    public SessionBattleResultHandler(Runnable onVictory, Runnable onDefeat) {
        this.onVictory = onVictory;
        this.onDefeat = onDefeat;
    }

    @Override
    public void onVictory(TrainerBattleParticipant trainer) {
        onVictory.run();
    }

    @Override
    public void onDefeat(TrainerBattleParticipant trainer) {
        onDefeat.run();
    }
}
