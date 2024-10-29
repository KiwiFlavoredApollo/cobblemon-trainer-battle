package kiwiapollo.cobblemontrainerbattle.datagen;

import com.cobblemon.mod.common.CobblemonItems;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.entities.EntityTypes;
import kiwiapollo.cobblemontrainerbattle.item.ItemRegistry;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.PlayerInteractedWithEntityCriterion;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class DataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(AdvancementProvider::new);
        pack.addProvider(RecipeProvider::new);
    }

    static class AdvancementProvider extends FabricAdvancementProvider {
        static final Identifier BACKGROUND = new Identifier("textures/gui/advancements/backgrounds/adventure.png");

        private static final PlayerInteractedWithEntityCriterion.Conditions CONDITIONS = new PlayerInteractedWithEntityCriterion.Conditions(
                LootContextPredicate.EMPTY,
                ItemPredicate.ANY,
                EntityPredicate.asLootContextPredicate(EntityPredicate.Builder.create().type(EntityTypes.TRAINER).build())
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

            for (Advancement advancement : new DefeatTrainerAdvancementFactory().create()) {
                consumer.accept(advancement);
            }

            for (Advancement advancement : new KillTrainerAdvancementFactory().create()) {
                consumer.accept(advancement);
            }

            for (Advancement advancement : new InclementEmeraldAdvancementFactory().create()) {
                consumer.accept(advancement);
            }
        }
    }

    private static class RecipeProvider extends FabricRecipeProvider {
        private RecipeProvider(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generate(Consumer<RecipeJsonProvider> exporter) {
            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.LEADER_ROXANNE_TICKET)
                    .input(ItemRegistry.TRAINER_TOKEN, 6)
                    .input(Items.GREEN_DYE)
                    .input(CobblemonItems.ROCK_GEM)
                    .criterion(FabricRecipeProvider.hasItem(ItemRegistry.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(ItemRegistry.TRAINER_TOKEN))
                    .criterion(FabricRecipeProvider.hasItem(CobblemonItems.ROCK_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.ROCK_GEM))
                    .offerTo(exporter);

            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.LEADER_BRAWLY_TICKET)
                    .input(ItemRegistry.TRAINER_TOKEN, 6)
                    .input(Items.GREEN_DYE)
                    .input(CobblemonItems.FIGHTING_GEM)
                    .criterion(FabricRecipeProvider.hasItem(ItemRegistry.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(ItemRegistry.TRAINER_TOKEN))
                    .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FIGHTING_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FIGHTING_GEM))
                    .offerTo(exporter);

            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.LEADER_WATTSON_TICKET)
                    .input(ItemRegistry.TRAINER_TOKEN, 6)
                    .input(Items.GREEN_DYE)
                    .input(CobblemonItems.ELECTRIC_GEM)
                    .criterion(FabricRecipeProvider.hasItem(ItemRegistry.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(ItemRegistry.TRAINER_TOKEN))
                    .criterion(FabricRecipeProvider.hasItem(CobblemonItems.ELECTRIC_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.ELECTRIC_GEM))
                    .offerTo(exporter);

            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.LEADER_FLANNERY_TICKET)
                    .input(ItemRegistry.TRAINER_TOKEN, 6)
                    .input(Items.GREEN_DYE)
                    .input(CobblemonItems.FIRE_GEM)
                    .criterion(FabricRecipeProvider.hasItem(ItemRegistry.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(ItemRegistry.TRAINER_TOKEN))
                    .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FIRE_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FIRE_GEM))
                    .offerTo(exporter);

            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.LEADER_NORMAN_TICKET)
                    .input(ItemRegistry.TRAINER_TOKEN, 6)
                    .input(Items.GREEN_DYE)
                    .input(CobblemonItems.NORMAL_GEM)
                    .criterion(FabricRecipeProvider.hasItem(ItemRegistry.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(ItemRegistry.TRAINER_TOKEN))
                    .criterion(FabricRecipeProvider.hasItem(CobblemonItems.NORMAL_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.NORMAL_GEM))
                    .offerTo(exporter);

            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.LEADER_WINONA_TICKET)
                    .input(ItemRegistry.TRAINER_TOKEN, 6)
                    .input(Items.GREEN_DYE)
                    .input(CobblemonItems.FLYING_GEM)
                    .criterion(FabricRecipeProvider.hasItem(ItemRegistry.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(ItemRegistry.TRAINER_TOKEN))
                    .criterion(FabricRecipeProvider.hasItem(CobblemonItems.FLYING_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.FLYING_GEM))
                    .offerTo(exporter);

            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.LEADER_TATE_AND_LIZA_TICKET)
                    .input(ItemRegistry.TRAINER_TOKEN, 6)
                    .input(Items.GREEN_DYE)
                    .input(CobblemonItems.PSYCHIC_GEM)
                    .criterion(FabricRecipeProvider.hasItem(ItemRegistry.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(ItemRegistry.TRAINER_TOKEN))
                    .criterion(FabricRecipeProvider.hasItem(CobblemonItems.PSYCHIC_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.PSYCHIC_GEM))
                    .offerTo(exporter);

            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.LEADER_JUAN_TICKET)
                    .input(ItemRegistry.TRAINER_TOKEN, 6)
                    .input(Items.GREEN_DYE)
                    .input(CobblemonItems.WATER_GEM)
                    .criterion(FabricRecipeProvider.hasItem(ItemRegistry.TRAINER_TOKEN), FabricRecipeProvider.conditionsFromItem(ItemRegistry.TRAINER_TOKEN))
                    .criterion(FabricRecipeProvider.hasItem(CobblemonItems.WATER_GEM), FabricRecipeProvider.conditionsFromItem(CobblemonItems.WATER_GEM))
                    .offerTo(exporter);
        }
    }
}
