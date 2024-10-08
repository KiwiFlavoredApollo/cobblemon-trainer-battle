package kiwiapollo.cobblemontrainerbattle.events;

import kiwiapollo.cobblemontrainerbattle.entities.TrainerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

public class TrainerEntityLoadEventHandler {
    public static void onEntityLoad(Entity entity, ServerWorld world) {
        if (entity instanceof TrainerEntity) {
            ((TrainerEntity) entity).synchronizeClient(world);
        }
    }
}
