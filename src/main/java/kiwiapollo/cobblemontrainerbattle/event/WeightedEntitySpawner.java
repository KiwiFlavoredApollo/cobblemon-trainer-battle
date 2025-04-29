package kiwiapollo.cobblemontrainerbattle.event;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public interface WeightedEntitySpawner {
    void spawnEntity(ServerWorld world, ServerPlayerEntity player);

    int getWeight();

    int getEntityCount(ServerWorld world, ServerPlayerEntity player);
}
