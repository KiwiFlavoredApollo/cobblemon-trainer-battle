package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.battleparticipants.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipants.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.exceptions.BattleStartException;

import java.util.UUID;

public interface TrainerBattle {
    void start() throws BattleStartException;

    void onPlayerVictory();

    void onPlayerDefeat();

    UUID getBattleId();

    PlayerBattleParticipant getPlayer();

    TrainerBattleParticipant getTrainer();
}
