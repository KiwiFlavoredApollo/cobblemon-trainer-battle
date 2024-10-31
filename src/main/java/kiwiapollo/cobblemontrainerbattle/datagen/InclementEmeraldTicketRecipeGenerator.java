package kiwiapollo.cobblemontrainerbattle.datagen;

import com.cobblemon.mod.common.CobblemonItems;
import kiwiapollo.cobblemontrainerbattle.item.ItemRegistry;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.function.Consumer;

public class InclementEmeraldTicketRecipeGenerator implements ItemRecipeGenerator {
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

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.ELITE_SIDNEY_TICKET)
                .input(ItemRegistry.LEADER_ROXANNE_TOKEN)
                .input(ItemRegistry.LEADER_BRAWLY_TOKEN)
                .input(CobblemonItems.BLACK_GLASSES)
                .criterion(FabricRecipeProvider.hasItem(ItemRegistry.LEADER_ROXANNE_TOKEN), FabricRecipeProvider.conditionsFromItem(ItemRegistry.LEADER_ROXANNE_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(ItemRegistry.LEADER_BRAWLY_TOKEN), FabricRecipeProvider.conditionsFromItem(ItemRegistry.LEADER_BRAWLY_TOKEN))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.ELITE_PHOEBE_TICKET)
                .input(ItemRegistry.LEADER_WATTSON_TOKEN)
                .input(ItemRegistry.LEADER_FLANNERY_TOKEN)
                .input(CobblemonItems.REAPER_CLOTH)
                .criterion(FabricRecipeProvider.hasItem(ItemRegistry.LEADER_WATTSON_TOKEN), FabricRecipeProvider.conditionsFromItem(ItemRegistry.LEADER_WATTSON_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(ItemRegistry.LEADER_FLANNERY_TOKEN), FabricRecipeProvider.conditionsFromItem(ItemRegistry.LEADER_FLANNERY_TOKEN))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.ELITE_GLACIA_TICKET)
                .input(ItemRegistry.LEADER_NORMAN_TOKEN)
                .input(ItemRegistry.LEADER_WINONA_TOKEN)
                .input(CobblemonItems.NEVER_MELT_ICE)
                .criterion(FabricRecipeProvider.hasItem(ItemRegistry.LEADER_NORMAN_TOKEN), FabricRecipeProvider.conditionsFromItem(ItemRegistry.LEADER_NORMAN_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(ItemRegistry.LEADER_WINONA_TOKEN), FabricRecipeProvider.conditionsFromItem(ItemRegistry.LEADER_WINONA_TOKEN))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.ELITE_DRAKE_TICKET)
                .input(ItemRegistry.LEADER_TATE_AND_LIZA_TOKEN)
                .input(ItemRegistry.LEADER_JUAN_TOKEN)
                .input(CobblemonItems.DRAGON_SCALE)
                .criterion(FabricRecipeProvider.hasItem(ItemRegistry.LEADER_TATE_AND_LIZA_TOKEN), FabricRecipeProvider.conditionsFromItem(ItemRegistry.LEADER_TATE_AND_LIZA_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(ItemRegistry.LEADER_JUAN_TOKEN), FabricRecipeProvider.conditionsFromItem(ItemRegistry.LEADER_JUAN_TOKEN))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemRegistry.CHAMPION_WALLACE_TICKET)
                .input(ItemRegistry.ELITE_SIDNEY_TOKEN)
                .input(ItemRegistry.ELITE_PHOEBE_TOKEN)
                .input(ItemRegistry.ELITE_GLACIA_TOKEN)
                .input(ItemRegistry.ELITE_DRAKE_TOKEN)
                .input(Items.NETHER_STAR)
                .criterion(FabricRecipeProvider.hasItem(ItemRegistry.ELITE_SIDNEY_TOKEN), FabricRecipeProvider.conditionsFromItem(ItemRegistry.ELITE_SIDNEY_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(ItemRegistry.ELITE_PHOEBE_TOKEN), FabricRecipeProvider.conditionsFromItem(ItemRegistry.ELITE_PHOEBE_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(ItemRegistry.ELITE_GLACIA_TOKEN), FabricRecipeProvider.conditionsFromItem(ItemRegistry.ELITE_GLACIA_TOKEN))
                .criterion(FabricRecipeProvider.hasItem(ItemRegistry.ELITE_DRAKE_TOKEN), FabricRecipeProvider.conditionsFromItem(ItemRegistry.ELITE_DRAKE_TOKEN))
                .offerTo(exporter);
    }
}
