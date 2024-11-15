package kiwiapollo.cobblemontrainerbattle.battle.groupbattle;

import kiwiapollo.cobblemontrainerbattle.battle.postbattle.PostBattleActionSet;

import java.util.List;

public class TrainerGroupProfile {
    public List<String> trainers;
    public boolean isRematchAllowed;
    public int maximumPartyLevel;
    public int minimumPartyLevel;
    public PostBattleActionSet onVictory;
    public PostBattleActionSet onDefeat;
}
