package kiwiapollo.cobblemontrainerbattle.entities;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattleClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class TrainerEntityRenderer extends MobEntityRenderer {

    public TrainerEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new TrainerEntityModel(context.getPart(CobblemonTrainerBattleClient.MODEL_TRAINER_LAYER)), 0.5f);
    }

    @Override
    public Identifier getTexture(Entity entity) {
        return Identifier.of("minecraft", "textures/entity/player/wide/steve.png");
    }
}
