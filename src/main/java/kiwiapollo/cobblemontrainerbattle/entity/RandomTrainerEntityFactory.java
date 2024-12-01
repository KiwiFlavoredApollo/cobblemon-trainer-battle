package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.battle.predicates.SpawningAllowedPredicate;
import kiwiapollo.cobblemontrainerbattle.common.RandomTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class RandomTrainerEntityFactory implements EntityType.EntityFactory<TrainerEntity> {
    private final SimpleFactory<Identifier> trainerFactory;

    public RandomTrainerEntityFactory() {
        this.trainerFactory = new RandomTrainerFactory.Builder().addAllTrainers().addPredicate(new SpawningAllowedPredicate()).build();
    }

    public RandomTrainerEntityFactory(RandomTrainerFactory trainerFactory) {
        this.trainerFactory = trainerFactory;
    }

    @Override
    public TrainerEntity create(EntityType<TrainerEntity> type, World world) {
        return new TrainerEntity(type, world, createRandomTrainer(), createRandomTexture());
    }

    private Identifier createRandomTrainer() {
        try {
            return trainerFactory.create();
        } catch (UnsupportedOperationException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    private Identifier createRandomTexture() {
        return new RandomTrainerTextureFactory().create();
    }
}
