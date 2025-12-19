package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.block.CustomBlock;
import kiwiapollo.cobblemontrainerbattle.block.PokeBallBoxBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Util;

public class CustomEntityType {
    public static final EntityType<NeutralTrainerEntity> NEUTRAL_TRAINER;
    public static final EntityType<StaticTrainerEntity> STATIC_TRAINER;
    public static final EntityType<TrainerEntity> TRAINER;
    public static final EntityType<MannequinEntity> MANNEQUIN;
    public static final BlockEntityType<PokeBallBoxBlockEntity> POKE_BALL_BOX;

    static {
        NEUTRAL_TRAINER = EntityType.Builder.create(new NeutralTrainerEntity.Factory(), SpawnGroup.CREATURE).setDimensions(0.6f, 1.8f).build("neutral_trainer");
        STATIC_TRAINER = EntityType.Builder.create(new StaticTrainerEntity.Factory(), SpawnGroup.CREATURE).setDimensions(0.6f, 1.8f).build("static_trainer");
        TRAINER = EntityType.Builder.create(new TrainerEntity.Factory(), SpawnGroup.CREATURE).setDimensions(0.6f, 1.8f).build("trainer");
        MANNEQUIN = EntityType.Builder.create(new MannequinEntity.Factory(), SpawnGroup.CREATURE).setDimensions(0.6f, 1.8f).build("mannequin");
        POKE_BALL_BOX = BlockEntityType.Builder.create(PokeBallBoxBlockEntity::new, CustomBlock.POKE_BALL_BOX).build(Util.getChoiceType(TypeReferences.BLOCK_ENTITY, "poke_ball_box"));
    }
}
