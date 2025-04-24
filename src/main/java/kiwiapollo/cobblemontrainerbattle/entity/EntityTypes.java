package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public class EntityTypes {
    public static final EntityType<TrainerEntity> TRAINER;
    public static final EntityType<AnchorEntity> ANCHOR;

    static {
        TRAINER = EntityType.Builder.create(new RandomTrainerEntityFactory(), SpawnGroup.CREATURE).setDimensions(0.6f, 1.8f).build("trainer");
        ANCHOR = EntityType.Builder.create(new RandomAnchorEntityFactory(), SpawnGroup.CREATURE).setDimensions(0.6f, 1.8f).build("anchor");
    }
}
