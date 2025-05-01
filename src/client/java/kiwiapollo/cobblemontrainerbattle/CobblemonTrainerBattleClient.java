package kiwiapollo.cobblemontrainerbattle;

import kiwiapollo.cobblemontrainerbattle.block.CustomScreenHandlerType;
import kiwiapollo.cobblemontrainerbattle.entity.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class CobblemonTrainerBattleClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(CustomEntityType.NEUTRAL_TRAINER, NeutralTrainerEntityRenderer::new);
		EntityRendererRegistry.register(CustomEntityType.HOSTILE_TRAINER, HostileTrainerEntityRenderer::new);
		EntityRendererRegistry.register(CustomEntityType.STATIC_TRAINER, StaticTrainerEntityRenderer::new);
		HandledScreens.register(CustomScreenHandlerType.TRAINER_TABLE, TrainerTableScreen::new);
	}
}