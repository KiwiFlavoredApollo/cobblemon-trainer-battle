package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class RandomHostileTrainerEntityFactory extends TrainerEntityFactory<HostileTrainerEntity> {
    @Override
    public HostileTrainerEntity create(EntityType<HostileTrainerEntity> type, World world) {
        HostileTrainerEntity entity = new HostileTrainerEntity(type, world);
        entity.setTrainer(createTrainer());
        return entity;
    }
}
