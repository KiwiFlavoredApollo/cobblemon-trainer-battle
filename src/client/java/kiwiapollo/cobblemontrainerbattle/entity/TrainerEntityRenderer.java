package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerStorage;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class TrainerEntityRenderer<T extends TrainerEntity> extends BipedEntityRenderer<T, PlayerEntityModel<T>> {
    private static final TrainerTexture FALLBACK_TEXTURE = TrainerTexture.RED;

    public TrainerEntityRenderer(EntityRendererFactory.Context context) {
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
