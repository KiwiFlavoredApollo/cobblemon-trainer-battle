package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class RandomStaticTrainerEntityFactory implements EntityType.EntityFactory<StaticTrainerEntity> {
    private final SimpleFactory<String> trainerFactory;

    public RandomStaticTrainerEntityFactory() {
        this(new RandomSpawnableTrainerFactory(trainer -> true));
    }

    public RandomStaticTrainerEntityFactory(SimpleFactory<String> trainerFactory) {
        this.trainerFactory = trainerFactory;
    }

    @Override
    public StaticTrainerEntity create(EntityType<StaticTrainerEntity> type, World world) {
        StaticTrainerEntity trainer = new StaticTrainerEntity(type, world);
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
