package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.common.RandomTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.parser.preset.TrainerStorage;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class RandomTrainerEntityFactory implements EntityType.EntityFactory<TrainerEntity> {
    private final RandomTrainerFactory trainerFactory;

    public RandomTrainerEntityFactory() {
        this.trainerFactory = new RandomTrainerFactory.Builder().addAll().build();
    }

    public RandomTrainerEntityFactory(RandomTrainerFactory trainerFactory) {
        this.trainerFactory = trainerFactory;
    }

    @Override
    public TrainerEntity create(EntityType<TrainerEntity> type, World world) {
        return new TrainerEntity(type, world, createRandomTrainer(), createRandomTexture());
    }

    private String createRandomTrainer() {
        try {
            String trainer;
            do {
                trainer = trainerFactory.create();
            } while (!isSpawningAllowed(trainer));

            return trainer;

        } catch (UnsupportedOperationException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    private boolean isSpawningAllowed(String trainer) {
        return TrainerStorage.getInstance().get(trainer).isSpawningAllowed();
    }

    private Identifier createRandomTexture() {
        return new RandomTrainerTextureFactory().create();
    }
}
