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
		EntityRendererRegistry.register(CustomEntityType.STATIC_TRAINER, StaticTrainerEntityRenderer::new);
		EntityRendererRegistry.register(CustomEntityType.CAMPER, CamperEntityRenderer::new);
		EntityRendererRegistry.register(CustomEntityType.DRIFTER, DrifterEntityRenderer::new);
		HandledScreens.register(CustomScreenHandlerType.POKE_BALL_BOX, PokeBallBoxScreen::new);
	}
}