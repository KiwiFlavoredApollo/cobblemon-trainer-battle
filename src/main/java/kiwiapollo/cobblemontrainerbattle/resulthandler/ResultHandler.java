package kiwiapollo.cobblemontrainerbattle.resulthandler;

import kiwiapollo.cobblemontrainerbattle.battleparticipant.TrainerBattleParticipant;

public interface ResultHandler {
    void onVictory(TrainerBattleParticipant trainer);

    void onDefeat(TrainerBattleParticipant trainer);
}
