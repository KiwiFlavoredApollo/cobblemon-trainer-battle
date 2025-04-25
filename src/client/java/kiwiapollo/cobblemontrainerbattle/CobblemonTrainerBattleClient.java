package kiwiapollo.cobblemontrainerbattle;

import kiwiapollo.cobblemontrainerbattle.entity.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class CobblemonTrainerBattleClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(EntityTypes.TRAINER, TrainerEntityRenderer::new);
		EntityRendererRegistry.register(EntityTypes.ANCHOR, AnchorEntityRenderer::new);
	}
}