package kiwiapollo.cobblemontrainerbattle.npc;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.TemplateModClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class TrainerEntityRenderer extends MobEntityRenderer {

    public TrainerEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new TrainerEntityModel(context.getPart(TemplateModClient.MODEL_TRAINER_LAYER)), 0.5f);
    }

    @Override
    public Identifier getTexture(Entity entity) {
        return Identifier.of(CobblemonTrainerBattle.NAMESPACE, "textures/entity/trainer/kiwileopard.png");
    }
}
