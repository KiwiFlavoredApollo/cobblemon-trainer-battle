package kiwiapollo.cobblemontrainerbattle.npc;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

public class TrainerEntity extends PathAwareEntity {

    public TrainerEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }
}
