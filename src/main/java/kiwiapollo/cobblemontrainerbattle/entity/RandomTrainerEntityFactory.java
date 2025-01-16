package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.common.RandomSpawnableTrainerFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class RandomTrainerEntityFactory implements EntityType.EntityFactory<TrainerEntity> {
    private final RandomSpawnableTrainerFactory trainer;
    private final RandomTrainerTextureFactory texture;

    public RandomTrainerEntityFactory() {
        this(new RandomSpawnableTrainerFactory(), new RandomTrainerTextureFactory());
    }

    public RandomTrainerEntityFactory(RandomSpawnableTrainerFactory trainer, RandomTrainerTextureFactory texture) {
        this.trainer = trainer;
        this.texture = texture;
    }

    @Override
    public TrainerEntity create(EntityType<TrainerEntity> type, World world) {
        return new TrainerEntity(type, world, createTrainer(), createTexture());
    }

    private String createTrainer() {
        try {
            return trainer.create();

        } catch (UnsupportedOperationException | NullPointerException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    private Identifier createTexture() {
        return texture.create();
    }
}
