package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class MannequinEntityRenderer extends BattleEntityRenderer<MannequinEntity> {
    private static final TrainerTexture FALLBACK_TEXTURE = TrainerTexture.LEAF;

    public MannequinEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected Identifier getFallbackTexture() {
        return FALLBACK_TEXTURE.getIdentifier();
    }
}
