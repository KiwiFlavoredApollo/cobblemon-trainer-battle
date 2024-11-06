package kiwiapollo.cobblemontrainerbattle.event;

import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

public class TrainerEntityLoadEventHandler {
    public static void synchronizeTrainerEntity(Entity entity, ServerWorld world) {
        if (entity instanceof TrainerEntity) {
            ((TrainerEntity) entity).synchronizeClient(world);
        }
    }
}
