package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.util.Identifier;

public class BattleEntityRenderer<T extends BattleEntity> extends BipedEntityRenderer<T, PlayerEntityModel<T>> {
    private static final TrainerTexture FALLBACK_TEXTURE = TrainerTexture.RED;

    public BattleEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new PlayerEntityModel<>(context.getPart(EntityModelLayers.PLAYER_SLIM), false), 0.5f);
    }

    @Override
    public Identifier getTexture(T entity) {
        try {
            return entity.getTexture();

        } catch (NullPointerException e) {
            return getFallbackTexture();
        }
    }

    protected Identifier getFallbackTexture() {
        return FALLBACK_TEXTURE.getIdentifier();
    }
}
