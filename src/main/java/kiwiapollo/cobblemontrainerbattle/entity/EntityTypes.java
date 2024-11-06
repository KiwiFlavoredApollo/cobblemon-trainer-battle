package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public class EntityTypes {
    public static final EntityType<TrainerEntity> TRAINER;

    static {
        TRAINER = EntityType.Builder.create(new RandomTrainerEntityFactory.Builder().addWildcard().build(), SpawnGroup.CREATURE).setDimensions(0.6f, 1.8f).build("trainer");
    }
}
