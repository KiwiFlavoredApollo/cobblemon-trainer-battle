package kiwiapollo.cobblemontrainerbattle.datagen;

import kiwiapollo.cobblemontrainerbattle.item.ItemTagRegistry;
import kiwiapollo.cobblemontrainerbattle.item.MiscItems;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.function.Consumer;

import static net.minecraft.data.server.recipe.RecipeProvider.getRecipeName;

public class VsSeekerRecipeGenerator implements ItemRecipeGenerator {
    private static final String VS_SEEKER_RECIPE_SUFFIX = "from_vs_seeker";

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, MiscItems.BLUE_VS_SEEKER)
                .pattern("IRI")
                .pattern("IBI")
                .pattern("III")
                .input('I', Items.IRON_INGOT)
                .input('R', Items.REDSTONE_TORCH)
                .input('B', Items.BLUE_STAINED_GLASS)
                .criterion(FabricRecipeProvider.hasItem(Items.REDSTONE_TORCH), FabricRecipeProvider.conditionsFromItem(Items.REDSTONE_TORCH))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, MiscItems.BLUE_VS_SEEKER)
                .input(ItemTagRegistry.VS_SEEKERS)
                .input(Items.BLUE_DYE)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.BLUE_VS_SEEKER), FabricRecipeProvider.conditionsFromItem(MiscItems.BLUE_VS_SEEKER))
                .offerTo(exporter, String.format("%s_%s", getRecipeName(MiscItems.BLUE_VS_SEEKER), VS_SEEKER_RECIPE_SUFFIX));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, MiscItems.RED_VS_SEEKER)
                .input(ItemTagRegistry.VS_SEEKERS)
                .input(Items.RED_DYE)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.BLUE_VS_SEEKER), FabricRecipeProvider.conditionsFromItem(MiscItems.BLUE_VS_SEEKER))
                .offerTo(exporter, String.format("%s_%s", getRecipeName(MiscItems.RED_VS_SEEKER), VS_SEEKER_RECIPE_SUFFIX));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, MiscItems.GREEN_VS_SEEKER)
                .input(ItemTagRegistry.VS_SEEKERS)
                .input(Items.GREEN_DYE)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.BLUE_VS_SEEKER), FabricRecipeProvider.conditionsFromItem(MiscItems.BLUE_VS_SEEKER))
                .offerTo(exporter, String.format("%s_%s", getRecipeName(MiscItems.GREEN_VS_SEEKER), VS_SEEKER_RECIPE_SUFFIX));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, MiscItems.PURPLE_VS_SEEKER)
                .input(ItemTagRegistry.VS_SEEKERS)
                .input(Items.PURPLE_DYE)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.BLUE_VS_SEEKER), FabricRecipeProvider.conditionsFromItem(MiscItems.BLUE_VS_SEEKER))
                .offerTo(exporter, String.format("%s_%s", getRecipeName(MiscItems.PURPLE_VS_SEEKER), VS_SEEKER_RECIPE_SUFFIX));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, MiscItems.PINK_VS_SEEKER)
                .input(ItemTagRegistry.VS_SEEKERS)
                .input(Items.PINK_DYE)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.BLUE_VS_SEEKER), FabricRecipeProvider.conditionsFromItem(MiscItems.BLUE_VS_SEEKER))
                .offerTo(exporter, String.format("%s_%s", getRecipeName(MiscItems.PINK_VS_SEEKER), VS_SEEKER_RECIPE_SUFFIX));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, MiscItems.YELLOW_VS_SEEKER)
                .input(ItemTagRegistry.VS_SEEKERS)
                .input(Items.YELLOW_DYE)
                .criterion(FabricRecipeProvider.hasItem(MiscItems.BLUE_VS_SEEKER), FabricRecipeProvider.conditionsFromItem(MiscItems.BLUE_VS_SEEKER))
                .offerTo(exporter, String.format("%s_%s", getRecipeName(MiscItems.YELLOW_VS_SEEKER), VS_SEEKER_RECIPE_SUFFIX));
    }
}
