package kiwiapollo.cobblemontrainerbattle.entity;

import net.minecraft.entity.SpawnGroup;

public class EntityType {
    public static final net.minecraft.entity.EntityType<NormalTrainerEntity> NORMAL_TRAINER;
    public static final net.minecraft.entity.EntityType<HostileTrainerEntity> HOSTILE_TRAINER;
    public static final net.minecraft.entity.EntityType<StaticTrainerEntity> STATIC_TRAINER;

    static {
        NORMAL_TRAINER = net.minecraft.entity.EntityType.Builder.create(new RandomNormalTrainerEntityFactory(), SpawnGroup.CREATURE).setDimensions(0.6f, 1.8f).build("normal_trainer");
        HOSTILE_TRAINER = net.minecraft.entity.EntityType.Builder.create(new RandomHostileTrainerEntityFactory(), SpawnGroup.CREATURE).setDimensions(0.6f, 1.8f).build("hostile_trainer");
        STATIC_TRAINER = net.minecraft.entity.EntityType.Builder.create(new RandomStaticTrainerEntityFactory(), SpawnGroup.CREATURE).setDimensions(0.6f, 1.8f).build("static_trainer");
    }
}
