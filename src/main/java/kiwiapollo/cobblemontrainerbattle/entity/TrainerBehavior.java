package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.util.Identifier;

public interface TrainerBehavior {
    void setTrainer(String trainer);

    Identifier getTexture();

    void onPlayerVictory();

    void onPlayerDefeat();
}
