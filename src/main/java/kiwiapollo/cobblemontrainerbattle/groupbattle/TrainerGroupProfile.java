package kiwiapollo.cobblemontrainerbattle.groupbattle;

import kiwiapollo.cobblemontrainerbattle.postbattle.PostBattleActionSet;

import java.util.List;

public class TrainerGroupProfile {
    public List<String> trainers;
    public boolean isRematchAllowed;
    public int maximumPartyLevel;
    public int minimumPartyLevel;
    public PostBattleActionSet onVictory;
    public PostBattleActionSet onDefeat;
}
