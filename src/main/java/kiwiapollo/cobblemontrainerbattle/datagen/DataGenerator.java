package kiwiapollo.cobblemontrainerbattle.datagen;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.PlayerInteractedWithEntityCriterion;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class DataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(AdvancementProvider::new);
    }

    static class AdvancementProvider extends FabricAdvancementProvider {
        static final Advancement ROOT = Advancement.Builder.createUntelemetered()
                .criterion("root", PlayerInteractedWithEntityCriterion.Conditions.create(
                        ItemPredicate.Builder.create().items(Items.AIR),
                        EntityPredicate.asLootContextPredicate(
                                EntityPredicate.Builder.create().type(CobblemonTrainerBattle.TRAINER_ENTITY_TYPE).build()
                        )
                ))
                .display(
                        Items.BELL,
                        Text.translatable("advancement.cobblemontrainerbattle.root.title"),
                        Text.translatable("advancement.cobblemontrainerbattle.root.description"),
                        new Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .build(Identifier.of(CobblemonTrainerBattle.NAMESPACE, "root"));

        protected AdvancementProvider(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generateAdvancement(Consumer<Advancement> consumer) {
            consumer.accept(ROOT);

            for (Advancement advancement : new DefeatTrainerAdvancementFactory().create()) {
                consumer.accept(advancement);
            }

            for (Advancement advancement : new KillTrainerAdvancementFactory().create()) {
                consumer.accept(advancement);
            }
        }
    }
}
