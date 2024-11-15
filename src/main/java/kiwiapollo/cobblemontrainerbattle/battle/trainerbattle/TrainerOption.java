package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.battle.postbattle.PostBattleActionSet;
import net.minecraft.sound.SoundEvent;

public class TrainerOption {
    public boolean isSpawningAllowed;
    public boolean isRematchAllowed;
    public int maximumPartyLevel;
    public int minimumPartyLevel;
    public SoundEvent battleTheme;
    public PostBattleActionSet onVictory;
    public PostBattleActionSet onDefeat;

    public TrainerOption() {
        this.isSpawningAllowed = true;
        this.isRematchAllowed = true;
        this.maximumPartyLevel = 100;
        this.minimumPartyLevel = 1;
        this.battleTheme = null;
        this.onVictory = new PostBattleActionSet();
        this.onDefeat = new PostBattleActionSet();
    }
}
