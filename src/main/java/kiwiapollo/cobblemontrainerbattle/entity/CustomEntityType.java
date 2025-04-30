package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public class CustomEntityType {
    public static final EntityType<NeutralTrainerEntity> NEUTRAL_TRAINER;
    public static final EntityType<HostileTrainerEntity> HOSTILE_TRAINER;
    public static final EntityType<StaticTrainerEntity> STATIC_TRAINER;

    static {
        NEUTRAL_TRAINER = EntityType.Builder.create(new RandomNeutralTrainerEntityFactory(), SpawnGroup.CREATURE).setDimensions(0.6f, 1.8f).build("neutral_trainer");
        HOSTILE_TRAINER = EntityType.Builder.create(new RandomHostileTrainerEntityFactory(), SpawnGroup.CREATURE).setDimensions(0.6f, 1.8f).build("hostile_trainer");
        STATIC_TRAINER = EntityType.Builder.create(new RandomStaticTrainerEntityFactory(), SpawnGroup.CREATURE).setDimensions(0.6f, 1.8f).build("static_trainer");
    }
}
