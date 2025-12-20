package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.block.CustomBlock;
import kiwiapollo.cobblemontrainerbattle.block.PokeBallBoxBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class CustomEntityType {
    public static final EntityType<NeutralTrainerEntity> NEUTRAL_TRAINER = register("neutral_trainer", EntityType.Builder.create(new NeutralTrainerEntity.Factory(), SpawnGroup.CREATURE).setDimensions(0.6f, 1.8f));
    public static final EntityType<StaticTrainerEntity> STATIC_TRAINER = register("static_trainer", EntityType.Builder.create(new StaticTrainerEntity.Factory(), SpawnGroup.CREATURE).setDimensions(0.6f, 1.8f));
    public static final EntityType<TrainerEntity> TRAINER = register("trainer", EntityType.Builder.create(new TrainerEntity.Factory(), SpawnGroup.CREATURE).setDimensions(0.6f, 1.8f));
    public static final EntityType<MannequinEntity> MANNEQUIN = register("mannequin", EntityType.Builder.create(new MannequinEntity.Factory(), SpawnGroup.CREATURE).setDimensions(0.6f, 1.8f));
    public static final BlockEntityType<PokeBallBoxBlockEntity> POKE_BALL_BOX = register("poke_ball_box", BlockEntityType.Builder.create(PokeBallBoxBlockEntity::new, CustomBlock.POKE_BALL_BOX));

    public static void initialize() {

    }

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> type) {
        Identifier identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, name);
        return Registry.register(Registries.ENTITY_TYPE, identifier, type.build(identifier.toString()));
    }

    @Deprecated
    // Poke Ball Box is messed up. It will be removed along with Poke Ball Engineer in the future.
    private static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType.Builder<T> type) {
        Identifier identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, "trainer_table");
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, identifier, type.build(Util.getChoiceType(TypeReferences.BLOCK_ENTITY, "poke_ball_box")));
    }

    static {
        FabricDefaultAttributeRegistry.register(CustomEntityType.NEUTRAL_TRAINER, NeutralTrainerEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(CustomEntityType.STATIC_TRAINER, StaticTrainerEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(CustomEntityType.TRAINER, TrainerEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(CustomEntityType.MANNEQUIN, MannequinEntity.createMobAttributes());
    }
}
