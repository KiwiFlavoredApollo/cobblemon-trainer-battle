package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.preset.TrainerTemplate;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class RandomStaticTrainerEntityFactory implements EntityType.EntityFactory<StaticTrainerEntity>  {
    private final RandomTrainerIdentifierFactory identifier;

    public RandomStaticTrainerEntityFactory() {
        this.identifier = new RandomTrainerIdentifierFactory(TrainerTemplate::isSpawningAllowed);
    }

    @Override
    public StaticTrainerEntity create(EntityType<StaticTrainerEntity> type, World world) {
        StaticTrainerEntity entity = new StaticTrainerEntity(type, world);
        entity.setTrainer(identifier.create());
        return entity;
    }
}
