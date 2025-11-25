package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.preset.TrainerTemplate;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class RandomNeutralTrainerEntityFactory implements EntityType.EntityFactory<NeutralTrainerEntity> {
    private final RandomTrainerIdentifierFactory identifier;

    public RandomNeutralTrainerEntityFactory() {
        this.identifier = new RandomTrainerIdentifierFactory(TrainerTemplate::isSpawningAllowed);
    }

    @Override
    public NeutralTrainerEntity create(EntityType<NeutralTrainerEntity> type, World world) {
        NeutralTrainerEntity entity = new NeutralTrainerEntity(type, world);
        entity.setTrainer(identifier.create());
        return entity;
    }
}
