package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;

import java.util.UUID;

public class NullTrainerBattle implements TrainerBattle {
    @Override
    public void start() throws BattleStartException {

    }

    @Override
    public void onPlayerVictory() {

    }

    @Override
    public void onPlayerDefeat() {

    }

    @Override
    public UUID getBattleId() {
        throw new NullPointerException();
    }

    @Override
    public PlayerBattleParticipant getPlayer() {
        throw new NullPointerException();
    }

    @Override
    public TrainerBattleParticipant getTrainer() {
        throw new NullPointerException();
    }
}
