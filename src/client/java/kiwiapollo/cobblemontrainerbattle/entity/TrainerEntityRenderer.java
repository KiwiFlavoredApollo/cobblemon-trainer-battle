package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class TrainerEntityRenderer extends BattleEntityRenderer<TrainerEntity> {
    private static final TrainerTexture FALLBACK_TEXTURE = TrainerTexture.LEAF;

    public TrainerEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected Identifier getFallbackTexture() {
        return FALLBACK_TEXTURE.getIdentifier();
    }
}
