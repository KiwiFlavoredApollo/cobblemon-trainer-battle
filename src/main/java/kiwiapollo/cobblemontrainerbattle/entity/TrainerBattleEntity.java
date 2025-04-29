package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.util.Identifier;

public interface TrainerBattleEntity {
    void setTrainer(String trainer);

    Identifier getTexture();

    void onPlayerVictory();

    void onPlayerDefeat();
}
