package kiwiapollo.cobblemontrainerbattle.groupbattle;

import kiwiapollo.cobblemontrainerbattle.common.BattleCondition;
import kiwiapollo.cobblemontrainerbattle.postbattle.PostBattleActionSet;

import java.util.List;

public class TrainerGroupProfile {
    public List<String> trainers;
    public BattleCondition condition;
    public PostBattleActionSet onVictory;
    public PostBattleActionSet onDefeat;
}
