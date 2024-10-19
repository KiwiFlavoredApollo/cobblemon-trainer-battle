package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.common.BattleCondition;
import kiwiapollo.cobblemontrainerbattle.postbattle.PostBattleActionSet;

public class TrainerOption {
    public BattleCondition condition;
    public PostBattleActionSet onVictory;
    public PostBattleActionSet onDefeat;

    public TrainerOption() {
        this.condition = new BattleCondition();
        this.onVictory = new PostBattleActionSet();
        this.onDefeat = new PostBattleActionSet();
    }
}
