package kiwiapollo.cobblemontrainerbattle.datagen;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.entity.CustomEntityType;
import kiwiapollo.cobblemontrainerbattle.item.ItemTagRegistry;
import kiwiapollo.cobblemontrainerbattle.item.VsSeekerItem;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.PlayerInteractedWithEntityCriterion;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class DataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(AdvancementProvider::new);
        pack.addProvider(RecipeProvider::new);
        pack.addProvider(PoiTagProvider::new);
        pack.addProvider(TagProvider::new);
        pack.addProvider(LootTableProvider::new);
        pack.addProvider(BlockTagProvider::new);
        pack.addProvider(TrainerEntityLootTableProvider::new);
    }

    static class AdvancementProvider extends FabricAdvancementProvider {
        static final Identifier BACKGROUND = Identifier.tryParse("textures/gui/advancements/backgrounds/adventure.png");

        private static final PlayerInteractedWithEntityCriterion.Conditions CONDITIONS = new PlayerInteractedWithEntityCriterion.Conditions(
                LootContextPredicate.EMPTY,
                ItemPredicate.ANY,
                EntityPredicate.asLootContextPredicate(EntityPredicate.Builder.create().type(CustomEntityType.NEUTRAL_TRAINER).build())
        );

        static final Advancement ROOT = Advancement.Builder.createUntelemetered()
                .criterion("root", CONDITIONS)
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
                .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "root"));

        protected AdvancementProvider(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generateAdvancement(Consumer<Advancement> consumer) {
            consumer.accept(ROOT);

            Arrays.stream(DefeatTrainerAdvancement.values()).map(CustomAdvancement::getAdvancement).forEach(consumer);
            Arrays.stream(KillTrainerAdvancement.values()).map(CustomAdvancement::getAdvancement).forEach(consumer);
            Arrays.stream(InclementEmeraldAdvancement.values()).map(CustomAdvancement::getAdvancement).forEach(consumer);
            Arrays.stream(RadicalRedAdvancement.values()).map(CustomAdvancement::getAdvancement).forEach(consumer);
            Arrays.stream(XyAdvancement.values()).map(CustomAdvancement::getAdvancement).forEach(consumer);
            Arrays.stream(BdspAdvancement.values()).map(CustomAdvancement::getAdvancement).forEach(consumer);
        }
    }

    private static class RecipeProvider extends FabricRecipeProvider {
        private RecipeProvider(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generate(Consumer<RecipeJsonProvider> exporter) {
            new VsSeekerRecipeGenerator().generate(exporter);
            new InclementEmeraldTicketRecipeGenerator().generate(exporter);
            new RadicalRedTicketRecipeGenerator().generate(exporter);
            new XyTicketRecipeGenerator().generate(exporter);
            new BdspTicketRecipeGenerator().generate(exporter);
        }
    }

    private static class TagProvider extends FabricTagProvider<Item> {
        public TagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.ITEM, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup lookup) {
            Arrays.stream(VsSeekerItem.values()).map(VsSeekerItem::getItem)
                    .forEach(item -> getOrCreateTagBuilder(ItemTagRegistry.VS_SEEKERS).add(item));
        }
    }
}
