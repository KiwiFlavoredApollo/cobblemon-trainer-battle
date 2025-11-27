package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class DrifterEntityRenderer extends TrainerEntityRenderer<DrifterEntity> {
    private static final TrainerTexture FALLBACK_TEXTURE = TrainerTexture.LEAF;

    public DrifterEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected Identifier getFallbackTexture() {
        return FALLBACK_TEXTURE.getIdentifier();
    }
}
