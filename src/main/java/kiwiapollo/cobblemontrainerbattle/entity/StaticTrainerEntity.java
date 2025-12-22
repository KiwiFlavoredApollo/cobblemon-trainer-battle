package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

public class StaticTrainerEntity extends MannequinEntity {
    public StaticTrainerEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);
    }
}
