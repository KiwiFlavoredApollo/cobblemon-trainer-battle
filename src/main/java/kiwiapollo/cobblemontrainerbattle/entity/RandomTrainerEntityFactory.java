package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class RandomTrainerEntityFactory implements EntityType.EntityFactory<TrainerEntity> {
    private final SimpleFactory<String> trainer;

    public RandomTrainerEntityFactory() {
        this(new RandomSpawnableTrainerFactory());
    }

    public RandomTrainerEntityFactory(SimpleFactory<String> trainer) {
        this.trainer = trainer;
    }

    @Override
    public TrainerEntity create(EntityType<TrainerEntity> type, World world) {
        return new TrainerEntity(type, world, createTrainer());
    }

    private String createTrainer() {
        try {
            return trainer.create();

        } catch (UnsupportedOperationException | NullPointerException | IndexOutOfBoundsException e) {
            return null;
        }
    }
}
