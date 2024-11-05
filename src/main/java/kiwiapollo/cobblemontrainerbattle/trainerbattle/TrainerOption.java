package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.common.BattleCondition;
import kiwiapollo.cobblemontrainerbattle.postbattle.PostBattleActionSet;
import net.minecraft.sound.SoundEvent;

public class TrainerOption {
    public boolean isSpawningAllowed;
    public BattleCondition condition;
    public SoundEvent battleTheme;
    public PostBattleActionSet onVictory;
    public PostBattleActionSet onDefeat;

    public TrainerOption() {
        this.condition = new BattleCondition();
        this.isSpawningAllowed = true;
        this.battleTheme = null;
        this.onVictory = new PostBattleActionSet();
        this.onDefeat = new PostBattleActionSet();
    }
}
