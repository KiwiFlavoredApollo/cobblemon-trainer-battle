package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public abstract class TrainerEntityFactory<T extends TrainerEntity> implements EntityType.EntityFactory<T> {
    private final SimpleFactory<Identifier> trainer;

    public TrainerEntityFactory() {
        this(new RandomSpawnableTrainerFactory(trainer -> true));
    }

    public TrainerEntityFactory(SimpleFactory<Identifier> trainer) {
        this.trainer = trainer;
    }

    public T create(EntityType<T> type, World world) {
        T entity = createEntity(type, world);
        entity.setTrainer(createTrainer());
        return entity;
    }

    protected abstract T createEntity(EntityType<T> type, World world);

    private Identifier createTrainer() {
        try {
            return trainer.create();

        } catch (UnsupportedOperationException | NullPointerException | IndexOutOfBoundsException e) {
            return null;
        }
    }
}
