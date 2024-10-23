package kiwiapollo.cobblemontrainerbattle.datagen;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.PlayerInteractWithTrainerCriterion;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.registry.Registries;
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
        static final Identifier BACKGROUND = new Identifier("textures/gui/advancements/backgrounds/adventure.png");

        static final Advancement ROOT = Advancement.Builder.createUntelemetered()
                .criterion("root", new PlayerInteractWithTrainerCriterion.Conditions())
                .display(
                        Registries.ITEM.get(Identifier.of("cobblemon", "link_cable")),
                        Text.translatable("advancement.cobblemontrainerbattle.root.title"),
                        Text.translatable("advancement.cobblemontrainerbattle.root.description"),
                        BACKGROUND,
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
