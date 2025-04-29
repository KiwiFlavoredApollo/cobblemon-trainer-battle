package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class RandomNormalTrainerEntityFactory extends TrainerEntityFactory<NormalTrainerEntity> {
    public RandomNormalTrainerEntityFactory() {
        super();
    }

    public RandomNormalTrainerEntityFactory(SimpleFactory<String> trainer) {
        super(trainer);
    }

    @Override
    protected NormalTrainerEntity createEntity(EntityType<NormalTrainerEntity> type, World world) {
        return new NormalTrainerEntity(type, world);
    }
}
