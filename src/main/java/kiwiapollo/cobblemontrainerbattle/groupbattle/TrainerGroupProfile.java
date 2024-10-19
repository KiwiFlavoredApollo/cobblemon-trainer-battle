package kiwiapollo.cobblemontrainerbattle.groupbattle;

import kiwiapollo.cobblemontrainerbattle.common.BattleCondition;
import kiwiapollo.cobblemontrainerbattle.resulthandler.ResultAction;

import java.util.List;

public class TrainerGroupProfile {
    public List<String> trainers;
    public BattleCondition condition;
    public ResultAction onVictory;
    public ResultAction onDefeat;
}
