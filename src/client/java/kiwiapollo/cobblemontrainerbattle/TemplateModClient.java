package kiwiapollo.cobblemontrainerbattle;

import kiwiapollo.cobblemontrainerbattle.npc.TrainerEntityModel;
import kiwiapollo.cobblemontrainerbattle.npc.TrainerEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class TemplateModClient implements ClientModInitializer {
	public static final EntityModelLayer MODEL_TRAINER_LAYER =
			new EntityModelLayer(Identifier.of(CobblemonTrainerBattle.NAMESPACE, "trainer"), "main");

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(CobblemonTrainerBattle.TRAINER_ENTITY_TYPE, TrainerEntityRenderer::new);

		EntityModelLayerRegistry.registerModelLayer(MODEL_TRAINER_LAYER, TrainerEntityModel::getTexturedModelData);
	}
}