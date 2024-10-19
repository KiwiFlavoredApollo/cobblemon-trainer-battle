package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.TrainerBattleParticipant;
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
