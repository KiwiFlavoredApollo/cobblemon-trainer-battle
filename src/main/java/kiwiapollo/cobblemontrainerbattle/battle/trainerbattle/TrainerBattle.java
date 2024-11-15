package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipant;
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
