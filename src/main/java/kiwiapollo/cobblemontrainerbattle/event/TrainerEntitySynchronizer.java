package kiwiapollo.cobblemontrainerbattle.event;

import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntity;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntityPackets;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class TrainerEntitySynchronizer implements ServerEntityEvents.Load {
    @Override
    public void onLoad(Entity entity, ServerWorld world) {
        if (!(entity instanceof TrainerEntity)) {
            return;
        }

        for (ServerPlayerEntity player : world.getPlayers()) {
            ServerPlayNetworking.send(player, TrainerEntityPackets.TRAINER_ENTITY_SYNC, ((TrainerEntity) entity).createPacket());
        }
    }
}
