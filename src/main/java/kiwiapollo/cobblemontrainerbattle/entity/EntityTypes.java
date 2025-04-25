package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public class EntityTypes {
    public static final EntityType<TrainerEntity> TRAINER;
    public static final EntityType<StaticTrainerEntity> STATIC_TRAINER;

    static {
        TRAINER = EntityType.Builder.create(new RandomTrainerEntityFactory(), SpawnGroup.CREATURE).setDimensions(0.6f, 1.8f).build("trainer");
        STATIC_TRAINER = EntityType.Builder.create(new RandomStaticTrainerEntityFactory(), SpawnGroup.CREATURE).setDimensions(0.6f, 1.8f).build("static_trainer");
    }
}
