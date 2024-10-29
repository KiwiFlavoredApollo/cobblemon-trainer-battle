package kiwiapollo.cobblemontrainerbattle.datagen;

import com.cobblemon.mod.common.CobblemonItems;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.entities.EntityTypes;
import kiwiapollo.cobblemontrainerbattle.item.ItemRegistry;
import kiwiapollo.cobblemontrainerbattle.item.ItemTagRegistry;
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
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class DataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(AdvancementProvider::new);
        pack.addProvider(RecipeProvider::new);
        pack.addProvider(TagProvider::new);
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

            ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.BLUE_VS_SEEKER)
                    .pattern("IRI")
                    .pattern("IBI")
                    .pattern("III")
                    .input('I', Items.IRON_INGOT)
                    .input('R', Items.REDSTONE_TORCH)
                    .input('B', Items.BLUE_STAINED_GLASS)
                    .criterion(FabricRecipeProvider.hasItem(Items.REDSTONE_TORCH), FabricRecipeProvider.conditionsFromItem(Items.REDSTONE_TORCH))
                    .offerTo(exporter);

            String VS_SEEKER_RECIPE_SUFFIX = "from_vs_seeker";

            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.BLUE_VS_SEEKER)
                    .input(ItemTagRegistry.VS_SEEKERS)
                    .input(Items.BLUE_DYE)
                    .criterion(FabricRecipeProvider.hasItem(ItemRegistry.BLUE_VS_SEEKER), FabricRecipeProvider.conditionsFromItem(ItemRegistry.BLUE_VS_SEEKER))
                    .offerTo(exporter, String.format("%s_%s", getRecipeName(ItemRegistry.BLUE_VS_SEEKER), VS_SEEKER_RECIPE_SUFFIX));

            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.RED_VS_SEEKER)
                    .input(ItemTagRegistry.VS_SEEKERS)
                    .input(Items.RED_DYE)
                    .criterion(FabricRecipeProvider.hasItem(ItemRegistry.BLUE_VS_SEEKER), FabricRecipeProvider.conditionsFromItem(ItemRegistry.BLUE_VS_SEEKER))
                    .offerTo(exporter, String.format("%s_%s", getRecipeName(ItemRegistry.RED_VS_SEEKER), VS_SEEKER_RECIPE_SUFFIX));

            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.GREEN_VS_SEEKER)
                    .input(ItemTagRegistry.VS_SEEKERS)
                    .input(Items.GREEN_DYE)
                    .criterion(FabricRecipeProvider.hasItem(ItemRegistry.BLUE_VS_SEEKER), FabricRecipeProvider.conditionsFromItem(ItemRegistry.BLUE_VS_SEEKER))
                    .offerTo(exporter, String.format("%s_%s", getRecipeName(ItemRegistry.GREEN_VS_SEEKER), VS_SEEKER_RECIPE_SUFFIX));

            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.PURPLE_VS_SEEKER)
                    .input(ItemTagRegistry.VS_SEEKERS)
                    .input(Items.PURPLE_DYE)
                    .criterion(FabricRecipeProvider.hasItem(ItemRegistry.BLUE_VS_SEEKER), FabricRecipeProvider.conditionsFromItem(ItemRegistry.BLUE_VS_SEEKER))
                    .offerTo(exporter, String.format("%s_%s", getRecipeName(ItemRegistry.PURPLE_VS_SEEKER), VS_SEEKER_RECIPE_SUFFIX));
        }
    }

    private static class TagProvider extends FabricTagProvider<Item> {
        public TagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.ITEM, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup lookup) {
            getOrCreateTagBuilder(ItemTagRegistry.VS_SEEKERS)
                    .add(ItemRegistry.BLUE_VS_SEEKER)
                    .add(ItemRegistry.RED_VS_SEEKER)
                    .add(ItemRegistry.GREEN_VS_SEEKER)
                    .add(ItemRegistry.PURPLE_VS_SEEKER);
        }
    }
}
