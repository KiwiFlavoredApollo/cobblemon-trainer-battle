package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class RandomAnchorEntityFactory implements EntityType.EntityFactory<AnchorEntity> {
    private final SimpleFactory<String> trainerFactory;

    public RandomAnchorEntityFactory() {
        this(new RandomSpawnableTrainerFactory(trainer -> true));
    }

    public RandomAnchorEntityFactory(SimpleFactory<String> trainerFactory) {
        this.trainerFactory = trainerFactory;
    }

    @Override
    public AnchorEntity create(EntityType<AnchorEntity> type, World world) {
        return new AnchorEntity(type, world, createTrainer());
    }

    private String createTrainer() {
        try {
            return trainerFactory.create();

        } catch (UnsupportedOperationException | NullPointerException | IndexOutOfBoundsException e) {
            return null;
        }
    }
}
