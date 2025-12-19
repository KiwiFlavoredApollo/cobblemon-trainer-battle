package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.util.Identifier;

public class NeutralTrainerEntityRenderer extends BipedEntityRenderer<NeutralTrainerEntity, PlayerEntityModel<NeutralTrainerEntity>> {
    private static final Identifier FALLBACK_TEXTURE = TrainerTexture.RED;

    public NeutralTrainerEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new PlayerEntityModel<>(context.getPart(EntityModelLayers.PLAYER_SLIM), false), 0.5f);
    }

    public Identifier getTexture(NeutralTrainerEntity entity) {
        try {
            return entity.getTexture();

        } catch (NullPointerException e) {
            return FALLBACK_TEXTURE;
        }
    }
}
