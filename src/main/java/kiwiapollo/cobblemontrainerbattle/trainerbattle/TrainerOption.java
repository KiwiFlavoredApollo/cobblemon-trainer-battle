package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.common.BattleCondition;
import kiwiapollo.cobblemontrainerbattle.resulthandler.ResultAction;

public class TrainerOption {
    public BattleCondition condition;
    public ResultAction onVictory;
    public ResultAction onDefeat;

    public TrainerOption() {
        this.condition = new BattleCondition();
        this.onVictory = new ResultAction();
        this.onDefeat = new ResultAction();
    }
}
