package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class RandomStaticTrainerEntityFactory extends TrainerEntityFactory<StaticTrainerEntity> {
    @Override
    protected StaticTrainerEntity createEntity(EntityType<StaticTrainerEntity> type, World world) {
        return new StaticTrainerEntity(type, world);
    }
}
