package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class RandomStaticTrainerEntityFactory extends TrainerEntityFactory<StaticTrainerEntity> {
    @Override
    public StaticTrainerEntity create(EntityType<StaticTrainerEntity> type, World world) {
        StaticTrainerEntity trainer = new StaticTrainerEntity(type, world);
        trainer.setTrainer(createTrainer());
        return trainer;
    }
}
