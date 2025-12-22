package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.util.Identifier;

public class TrainerEntityRenderer extends BipedEntityRenderer<TrainerEntity, PlayerEntityModel<TrainerEntity>> {
    public TrainerEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new PlayerEntityModel<>(context.getPart(EntityModelLayers.PLAYER_SLIM), false), 0.5f);
    }

    public Identifier getTexture(TrainerEntity entity) {
        return entity.getTexture();
    }
}
