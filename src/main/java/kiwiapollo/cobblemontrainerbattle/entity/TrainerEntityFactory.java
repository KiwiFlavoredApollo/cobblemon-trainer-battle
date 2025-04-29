package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

public abstract class TrainerEntityFactory<T extends TrainerEntity> implements EntityType.EntityFactory<T> {
    private final SimpleFactory<String> trainerFactory;

    public TrainerEntityFactory() {
        this(new RandomSpawnableTrainerFactory(trainer -> true));
    }

    public TrainerEntityFactory(SimpleFactory<String> trainerFactory) {
        this.trainerFactory = trainerFactory;
    }

    protected String createTrainer() {
        try {
            return trainerFactory.create();

        } catch (UnsupportedOperationException | NullPointerException | IndexOutOfBoundsException e) {
            return null;
        }
    }
}
