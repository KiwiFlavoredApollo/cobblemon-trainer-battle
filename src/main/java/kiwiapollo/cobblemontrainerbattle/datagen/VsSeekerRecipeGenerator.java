package kiwiapollo.cobblemontrainerbattle.datagen;

import kiwiapollo.cobblemontrainerbattle.item.ItemTagRegistry;
import kiwiapollo.cobblemontrainerbattle.item.VsSeekerItem;
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
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, VsSeekerItem.BLUE_VS_SEEKER.getItem())
                .pattern("IRI")
                .pattern("IBI")
                .pattern("III")
                .input('I', Items.IRON_INGOT)
                .input('R', Items.REDSTONE)
                .input('B', Items.BLUE_STAINED_GLASS)
                .criterion(FabricRecipeProvider.hasItem(Items.REDSTONE_TORCH), FabricRecipeProvider.conditionsFromItem(Items.REDSTONE_TORCH))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, VsSeekerItem.BLUE_VS_SEEKER.getItem())
                .input(ItemTagRegistry.VS_SEEKERS)
                .input(Items.BLUE_DYE)
                .criterion(FabricRecipeProvider.hasItem(VsSeekerItem.BLUE_VS_SEEKER.getItem()), FabricRecipeProvider.conditionsFromItem(VsSeekerItem.BLUE_VS_SEEKER.getItem()))
                .offerTo(exporter, String.format("%s_%s", getRecipeName(VsSeekerItem.BLUE_VS_SEEKER.getItem()), VS_SEEKER_RECIPE_SUFFIX));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, VsSeekerItem.RED_VS_SEEKER.getItem())
                .input(ItemTagRegistry.VS_SEEKERS)
                .input(Items.RED_DYE)
                .criterion(FabricRecipeProvider.hasItem(VsSeekerItem.BLUE_VS_SEEKER.getItem()), FabricRecipeProvider.conditionsFromItem(VsSeekerItem.BLUE_VS_SEEKER.getItem()))
                .offerTo(exporter, String.format("%s_%s", getRecipeName(VsSeekerItem.RED_VS_SEEKER.getItem()), VS_SEEKER_RECIPE_SUFFIX));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, VsSeekerItem.GREEN_VS_SEEKER.getItem())
                .input(ItemTagRegistry.VS_SEEKERS)
                .input(Items.GREEN_DYE)
                .criterion(FabricRecipeProvider.hasItem(VsSeekerItem.BLUE_VS_SEEKER.getItem()), FabricRecipeProvider.conditionsFromItem(VsSeekerItem.BLUE_VS_SEEKER.getItem()))
                .offerTo(exporter, String.format("%s_%s", getRecipeName(VsSeekerItem.GREEN_VS_SEEKER.getItem()), VS_SEEKER_RECIPE_SUFFIX));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, VsSeekerItem.PURPLE_VS_SEEKER.getItem())
                .input(ItemTagRegistry.VS_SEEKERS)
                .input(Items.PURPLE_DYE)
                .criterion(FabricRecipeProvider.hasItem(VsSeekerItem.BLUE_VS_SEEKER.getItem()), FabricRecipeProvider.conditionsFromItem(VsSeekerItem.BLUE_VS_SEEKER.getItem()))
                .offerTo(exporter, String.format("%s_%s", getRecipeName(VsSeekerItem.PURPLE_VS_SEEKER.getItem()), VS_SEEKER_RECIPE_SUFFIX));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, VsSeekerItem.PINK_VS_SEEKER.getItem())
                .input(ItemTagRegistry.VS_SEEKERS)
                .input(Items.PINK_DYE)
                .criterion(FabricRecipeProvider.hasItem(VsSeekerItem.BLUE_VS_SEEKER.getItem()), FabricRecipeProvider.conditionsFromItem(VsSeekerItem.BLUE_VS_SEEKER.getItem()))
                .offerTo(exporter, String.format("%s_%s", getRecipeName(VsSeekerItem.PINK_VS_SEEKER.getItem()), VS_SEEKER_RECIPE_SUFFIX));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, VsSeekerItem.YELLOW_VS_SEEKER.getItem())
                .input(ItemTagRegistry.VS_SEEKERS)
                .input(Items.YELLOW_DYE)
                .criterion(FabricRecipeProvider.hasItem(VsSeekerItem.BLUE_VS_SEEKER.getItem()), FabricRecipeProvider.conditionsFromItem(VsSeekerItem.BLUE_VS_SEEKER.getItem()))
                .offerTo(exporter, String.format("%s_%s", getRecipeName(VsSeekerItem.YELLOW_VS_SEEKER.getItem()), VS_SEEKER_RECIPE_SUFFIX));
    }
}
