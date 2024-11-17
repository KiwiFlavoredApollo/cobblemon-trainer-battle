package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.parser.history.*;

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
        updateVictoryRecord();
    }

    @Override
    public void onPlayerDefeat() {
        battle.onPlayerDefeat();
        updateDefeatRecord();
    }

    private BattleRecord getBattleRecord() {
        return (BattleRecord) PlayerHistoryManager.get(getPlayer().getUuid()).get(getTrainer().getIdentifier());
    }

    private void updateVictoryRecord() {
        getBattleRecord().setVictoryCount(getBattleRecord().getVictoryCount() + 1);
    }

    private void updateDefeatRecord() {
        getBattleRecord().setDefeatCount(getBattleRecord().getDefeatCount() + 1);
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
