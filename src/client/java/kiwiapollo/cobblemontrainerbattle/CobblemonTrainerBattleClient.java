package kiwiapollo.cobblemontrainerbattle;

import kiwiapollo.cobblemontrainerbattle.entities.TrainerEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class CobblemonTrainerBattleClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(CobblemonTrainerBattle.TRAINER_ENTITY_TYPE, TrainerEntityRenderer::new);
	}
}