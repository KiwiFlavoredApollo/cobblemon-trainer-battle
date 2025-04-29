package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class RandomNormalTrainerEntityFactory extends TrainerEntityFactory<NormalTrainerEntity> {
    public RandomNormalTrainerEntityFactory() {
        super();
    }

    public RandomNormalTrainerEntityFactory(SimpleFactory<String> trainerFactory) {
        super(trainerFactory);
    }

    @Override
    public NormalTrainerEntity create(EntityType<NormalTrainerEntity> type, World world) {
        NormalTrainerEntity trainer = new NormalTrainerEntity(type, world);
        trainer.setTrainer(createTrainer());
        return trainer;
    }
}
