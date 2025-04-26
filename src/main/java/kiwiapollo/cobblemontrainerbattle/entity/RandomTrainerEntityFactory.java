package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class RandomTrainerEntityFactory implements EntityType.EntityFactory<TrainerEntity> {
    private final SimpleFactory<String> trainerFactory;

    public RandomTrainerEntityFactory() {
        this(new RandomSpawnableTrainerFactory(trainer -> true));
    }

    public RandomTrainerEntityFactory(SimpleFactory<String> trainerFactory) {
        this.trainerFactory = trainerFactory;
    }

    @Override
    public TrainerEntity create(EntityType<TrainerEntity> type, World world) {
        TrainerEntity trainer = new TrainerEntity(type, world);
        trainer.setTrainer(createTrainer());
        return trainer;
    }

    private String createTrainer() {
        try {
            return trainerFactory.create();

        } catch (UnsupportedOperationException | NullPointerException | IndexOutOfBoundsException e) {
            return null;
        }
    }
}
