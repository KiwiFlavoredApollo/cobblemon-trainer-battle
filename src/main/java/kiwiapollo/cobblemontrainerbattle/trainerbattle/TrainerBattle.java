package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.battleparticipants.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipants.TrainerBattleParticipant;

import java.util.UUID;

public interface TrainerBattle {
    void start() throws BattleStartException;

    void onPlayerVictory();

    void onPlayerDefeat();

    UUID getBattleId();

    PlayerBattleParticipant getPlayer();

    TrainerBattleParticipant getTrainer();
}
