package kiwiapollo.cobblemontrainerbattle.battle.battlefactory;

import kiwiapollo.cobblemontrainerbattle.battle.postbattle.PostBattleActionSet;
import net.minecraft.sound.SoundEvent;

public class BattleFactoryProfile {
    public String battleAI;
    public SoundEvent battleTheme;
    public PostBattleActionSet onVictory;
    public PostBattleActionSet onDefeat;
}
