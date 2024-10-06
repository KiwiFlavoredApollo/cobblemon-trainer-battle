package kiwiapollo.cobblemontrainerbattle.entities;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.util.Identifier;

public class TrainerEntityRenderer extends MobEntityRenderer<TrainerEntity, PlayerEntityModel<TrainerEntity>> {

    public TrainerEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new PlayerEntityModel<>(context.getPart(EntityModelLayers.PLAYER), false), 0.5f);
    }

    @Override
    public Identifier getTexture(TrainerEntity entity) {
        return Identifier.of("minecraft", "textures/entity/player/wide/steve.png");
    }
}
