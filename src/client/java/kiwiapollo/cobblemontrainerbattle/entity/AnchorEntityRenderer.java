package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.util.Identifier;

public class AnchorEntityRenderer extends MobEntityRenderer<AnchorEntity, PlayerEntityModel<AnchorEntity>> {
    private final TrainerEntityRenderer renderer;

    public AnchorEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new PlayerEntityModel<>(context.getPart(EntityModelLayers.PLAYER_SLIM), false), 0.5f);
        this.renderer = new TrainerEntityRenderer(context);
    }

    @Override
    public Identifier getTexture(AnchorEntity entity) {
        return renderer.getTexture(entity);
    }
}
