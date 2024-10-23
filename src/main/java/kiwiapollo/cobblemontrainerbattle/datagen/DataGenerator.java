package kiwiapollo.cobblemontrainerbattle.datagen;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.DefeatTrainerCriterion;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityPredicate;
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

        protected AdvancementProvider(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generateAdvancement(Consumer<Advancement> consumer) {
            Advancement root = Advancement.Builder.createUntelemetered()
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
                    .criterion("defeat_trainer", new DefeatTrainerCriterion.Conditions())
                    .build(Identifier.of(CobblemonTrainerBattle.NAMESPACE, "root"));

            Advancement murderer = Advancement.Builder.createUntelemetered()
                    .display(
                            Items.IRON_SWORD,
                            Text.translatable("advancement.cobblemontrainerbattle.murderer.title"),
                            Text.translatable("advancement.cobblemontrainerbattle.murderer.description"),
                            new Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                            AdvancementFrame.TASK,
                            true,
                            true,
                            false
                    )
                    .parent(root)
                    .criterion("kill_trainer", OnKilledCriterion.Conditions.createPlayerKilledEntity(
                            new EntityPredicate.Builder().type(CobblemonTrainerBattle.TRAINER_ENTITY_TYPE).build()
                    ))
                    .build(Identifier.of(CobblemonTrainerBattle.NAMESPACE, "murderer"));

            consumer.accept(root);
            consumer.accept(murderer);
        }
    }
}
