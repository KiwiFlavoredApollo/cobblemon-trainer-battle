package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class RandomHostileTrainerEntityFactory extends TrainerEntityFactory<HostileTrainerEntity> {
    public RandomHostileTrainerEntityFactory() {
        super();
    }

    public RandomHostileTrainerEntityFactory(SimpleFactory<Identifier> trainer) {
        super(trainer);
    }

    @Override
    protected HostileTrainerEntity createEntity(EntityType<HostileTrainerEntity> type, World world) {
        return new HostileTrainerEntity(type, world);
    }
}
