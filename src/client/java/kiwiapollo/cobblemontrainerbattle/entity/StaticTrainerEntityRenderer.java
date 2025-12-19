package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class StaticTrainerEntityRenderer extends BattleEntityRenderer<StaticTrainerEntity> {
    private static final TrainerTexture FALLBACK_TEXTURE = TrainerTexture.LEAF;

    public StaticTrainerEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected Identifier getFallbackTexture() {
        return FALLBACK_TEXTURE.getIdentifier();
    }
}
