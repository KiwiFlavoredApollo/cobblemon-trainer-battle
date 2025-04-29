package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class HostileTrainerEntityRenderer extends TrainerEntityRenderer<HostileTrainerEntity> {
    private static final TrainerTexture FALLBACK_TEXTURE = TrainerTexture.GREEN;

    public HostileTrainerEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected Identifier getFallbackTexture() {
        return FALLBACK_TEXTURE.getIdentifier();
    }
}
