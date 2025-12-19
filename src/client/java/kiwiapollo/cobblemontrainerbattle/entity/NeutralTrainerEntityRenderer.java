package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class NeutralTrainerEntityRenderer extends BattleEntityRenderer<NeutralTrainerEntity> {
    private static final TrainerTexture FALLBACK_TEXTURE = TrainerTexture.RED;

    public NeutralTrainerEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected Identifier getFallbackTexture() {
        return FALLBACK_TEXTURE.getIdentifier();
    }
}
