package kiwiapollo.cobblemontrainerbattle.event;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public interface EntityCounter {
    int getEntityCount(ServerWorld world, ServerPlayerEntity player);
}
