package kiwiapollo.cobblemontrainerbattle.datagen;

import com.cobblemon.mod.common.CobblemonItems;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.item.MiscItems;
import kiwiapollo.cobblemontrainerbattle.loot.DefeatedInBattleLootCondition;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.KilledByPlayerLootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class TrainerEntityLootTableProvider extends SimpleFabricLootTableProvider {
    public TrainerEntityLootTableProvider(FabricDataOutput output) {
        super(output, LootContextTypes.ENTITY);
    }

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {
        exporter.accept(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entities/trainer"), LootTable.builder()
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0f))
                        .with(ItemEntry.builder(MiscItems.TRAINER_TOKEN).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0f))))
                        .conditionally(new DefeatedInBattleLootCondition())
                        .conditionally(RandomChanceLootCondition.builder(0.5f))
                        .build())
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0f))
                        .with(ItemEntry.builder(CobblemonItems.POKE_BALL).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0f))))
                        .conditionally(KilledByPlayerLootCondition.builder().build())
                        .conditionally(RandomChanceLootCondition.builder(0.5f))
                        .build())
        );
    }
}
