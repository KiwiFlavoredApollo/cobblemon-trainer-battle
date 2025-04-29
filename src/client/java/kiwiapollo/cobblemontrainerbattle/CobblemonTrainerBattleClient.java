package kiwiapollo.cobblemontrainerbattle;

import kiwiapollo.cobblemontrainerbattle.entity.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class CobblemonTrainerBattleClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(EntityTypes.NORMAL_TRAINER, NormalTrainerEntityRenderer::new);
		EntityRendererRegistry.register(EntityTypes.HOSTILE_TRAINER, HostileTrainerEntityRenderer::new);
		EntityRendererRegistry.register(EntityTypes.STATIC_TRAINER, StaticTrainerEntityRenderer::new);
	}
}