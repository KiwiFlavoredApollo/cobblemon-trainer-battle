package kiwiapollo.cobblemontrainerbattle.event;

import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntity;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

public class TrainerEntitySynchronizer implements ServerEntityEvents.Load {
    @Override
    public void onLoad(Entity entity, ServerWorld world) {
        if (entity instanceof TrainerEntity) {
            ((TrainerEntity) entity).synchronizeClient(world);
        }
    }
}
