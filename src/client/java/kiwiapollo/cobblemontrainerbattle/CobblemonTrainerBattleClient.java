package kiwiapollo.cobblemontrainerbattle;

import kiwiapollo.cobblemontrainerbattle.entity.EntityTypes;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntity;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntityPackets;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class CobblemonTrainerBattleClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(EntityTypes.TRAINER, TrainerEntityRenderer::new);

		ClientPlayNetworking.registerGlobalReceiver(TrainerEntityPackets.TRAINER_ENTITY_SYNC, (client, handler, buf, responseSender) -> {
			int entityId = buf.readInt();
			String trainer = buf.readString();
			Identifier texture = buf.readIdentifier();

			client.execute(() -> {
				Entity entity = client.world.getEntityById(entityId);
				if (entity instanceof TrainerEntity) {
					((TrainerEntity) entity).setTrainer(trainer);
					((TrainerEntity) entity).setTexture(texture);
				}
			});
		});
	}
}