package kiwiapollo.cobblemontrainerbattle;

import kiwiapollo.cobblemontrainerbattle.entity.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class CobblemonTrainerBattleClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(EntityType.NORMAL_TRAINER, NormalTrainerEntityRenderer::new);
		EntityRendererRegistry.register(EntityType.HOSTILE_TRAINER, HostileTrainerEntityRenderer::new);
		EntityRendererRegistry.register(EntityType.STATIC_TRAINER, StaticTrainerEntityRenderer::new);
	}
}