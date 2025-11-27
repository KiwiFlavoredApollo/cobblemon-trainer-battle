package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class CamperEntityRenderer extends TrainerEntityRenderer<CamperEntity> {
    private static final TrainerTexture FALLBACK_TEXTURE = TrainerTexture.LEAF;

    public CamperEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected Identifier getFallbackTexture() {
        return FALLBACK_TEXTURE.getIdentifier();
    }
}
