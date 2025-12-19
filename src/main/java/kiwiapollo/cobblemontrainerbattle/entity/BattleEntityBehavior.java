package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.util.Identifier;

import java.util.UUID;

public interface BattleEntityBehavior {
    void setTrainer(Identifier trainer);

    Identifier getTexture();

    UUID getBattleId();

    void onPlayerVictory();

    void onPlayerDefeat();
}
