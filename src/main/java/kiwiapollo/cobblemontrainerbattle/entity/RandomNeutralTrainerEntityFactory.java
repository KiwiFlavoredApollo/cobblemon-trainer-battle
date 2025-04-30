package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class RandomNeutralTrainerEntityFactory extends TrainerEntityFactory<NeutralTrainerEntity> {
    public RandomNeutralTrainerEntityFactory() {
        super();
    }

    public RandomNeutralTrainerEntityFactory(SimpleFactory<String> trainer) {
        super(trainer);
    }

    @Override
    protected NeutralTrainerEntity createEntity(EntityType<NeutralTrainerEntity> type, World world) {
        return new NeutralTrainerEntity(type, world);
    }
}
