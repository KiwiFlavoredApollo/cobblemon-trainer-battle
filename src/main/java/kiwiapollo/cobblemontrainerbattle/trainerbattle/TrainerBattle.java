package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.battleparticipant.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;

import java.util.UUID;

public interface TrainerBattle {
    void start() throws BattleStartException;

    void onPlayerVictory();

    void onPlayerDefeat();

    UUID getBattleId();

    PlayerBattleParticipant getPlayer();

    TrainerBattleParticipant getTrainer();
}
