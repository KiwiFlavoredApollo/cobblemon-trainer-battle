package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.parser.history.PlayerHistoryManager;

import java.util.UUID;

public class RecordedTrainerBattle implements TrainerBattle {
    private final TrainerBattle battle;

    public RecordedTrainerBattle(TrainerBattle battle) {
        this.battle = battle;
    }

    @Override
    public void start() throws BattleStartException {
        battle.start();
    }

    @Override
    public void onPlayerVictory() {
        battle.onPlayerVictory();
        PlayerHistoryManager.get(getPlayer().getUuid()).addPlayerVictory(getTrainer().getIdentifier());
    }

    @Override
    public void onPlayerDefeat() {
        battle.onPlayerDefeat();
        PlayerHistoryManager.get(getPlayer().getUuid()).addPlayerDefeat(getTrainer().getIdentifier());
    }

    @Override
    public UUID getBattleId() {
        return battle.getBattleId();
    }

    @Override
    public PlayerBattleParticipant getPlayer() {
        return battle.getPlayer();
    }

    @Override
    public TrainerBattleParticipant getTrainer() {
        return battle.getTrainer();
    }
}
