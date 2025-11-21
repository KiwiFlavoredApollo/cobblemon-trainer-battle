package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import net.minecraft.util.Identifier;

public interface TrainerEntityBehavior {
    void setTrainer(Identifier trainer);

    Identifier getTexture();

    TrainerBattle getTrainerBattle();

    void onPlayerVictory();

    void onPlayerDefeat();
}
