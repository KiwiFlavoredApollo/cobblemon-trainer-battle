package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class NormalTrainerEntityRenderer extends TrainerEntityRenderer<NormalTrainerEntity> {
    private static final TrainerTexture FALLBACK_TEXTURE = TrainerTexture.RED;

    public NormalTrainerEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected Identifier getFallbackTexture() {
        return FALLBACK_TEXTURE.getIdentifier();
    }
}
