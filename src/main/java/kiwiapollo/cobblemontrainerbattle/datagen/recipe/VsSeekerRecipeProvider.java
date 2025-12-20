package kiwiapollo.cobblemontrainerbattle.datagen.recipe;

import kiwiapollo.cobblemontrainerbattle.datagen.tag.ItemTagRegistry;
import kiwiapollo.cobblemontrainerbattle.item.vsseeker.VsSeekerItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.function.Consumer;

import static net.minecraft.data.server.recipe.RecipeProvider.getRecipeName;

public class VsSeekerRecipeProvider extends FabricRecipeProvider {
    private static final String VS_SEEKER_RECIPE_SUFFIX = "from_vs_seeker";

    public VsSeekerRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, VsSeekerItem.BLUE_VS_SEEKER)
                .pattern("IRI")
                .pattern("IBI")
                .pattern("III")
                .input('I', Items.IRON_INGOT)
                .input('R', Items.REDSTONE)
                .input('B', Items.BLUE_STAINED_GLASS)
                .criterion(FabricRecipeProvider.hasItem(Items.REDSTONE_TORCH), FabricRecipeProvider.conditionsFromItem(Items.REDSTONE_TORCH))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, VsSeekerItem.BLUE_VS_SEEKER)
                .input(ItemTagRegistry.VS_SEEKERS)
                .input(Items.BLUE_DYE)
                .criterion(FabricRecipeProvider.hasItem(VsSeekerItem.BLUE_VS_SEEKER), FabricRecipeProvider.conditionsFromItem(VsSeekerItem.BLUE_VS_SEEKER))
                .offerTo(exporter, String.format("%s_%s", getRecipeName(VsSeekerItem.BLUE_VS_SEEKER), VS_SEEKER_RECIPE_SUFFIX));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, VsSeekerItem.RED_VS_SEEKER)
                .input(ItemTagRegistry.VS_SEEKERS)
                .input(Items.RED_DYE)
                .criterion(FabricRecipeProvider.hasItem(VsSeekerItem.BLUE_VS_SEEKER), FabricRecipeProvider.conditionsFromItem(VsSeekerItem.BLUE_VS_SEEKER))
                .offerTo(exporter, String.format("%s_%s", getRecipeName(VsSeekerItem.RED_VS_SEEKER), VS_SEEKER_RECIPE_SUFFIX));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, VsSeekerItem.GREEN_VS_SEEKER)
                .input(ItemTagRegistry.VS_SEEKERS)
                .input(Items.GREEN_DYE)
                .criterion(FabricRecipeProvider.hasItem(VsSeekerItem.BLUE_VS_SEEKER), FabricRecipeProvider.conditionsFromItem(VsSeekerItem.BLUE_VS_SEEKER))
                .offerTo(exporter, String.format("%s_%s", getRecipeName(VsSeekerItem.GREEN_VS_SEEKER), VS_SEEKER_RECIPE_SUFFIX));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, VsSeekerItem.PURPLE_VS_SEEKER)
                .input(ItemTagRegistry.VS_SEEKERS)
                .input(Items.PURPLE_DYE)
                .criterion(FabricRecipeProvider.hasItem(VsSeekerItem.BLUE_VS_SEEKER), FabricRecipeProvider.conditionsFromItem(VsSeekerItem.BLUE_VS_SEEKER))
                .offerTo(exporter, String.format("%s_%s", getRecipeName(VsSeekerItem.PURPLE_VS_SEEKER), VS_SEEKER_RECIPE_SUFFIX));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, VsSeekerItem.PINK_VS_SEEKER)
                .input(ItemTagRegistry.VS_SEEKERS)
                .input(Items.PINK_DYE)
                .criterion(FabricRecipeProvider.hasItem(VsSeekerItem.BLUE_VS_SEEKER), FabricRecipeProvider.conditionsFromItem(VsSeekerItem.BLUE_VS_SEEKER))
                .offerTo(exporter, String.format("%s_%s", getRecipeName(VsSeekerItem.PINK_VS_SEEKER), VS_SEEKER_RECIPE_SUFFIX));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, VsSeekerItem.YELLOW_VS_SEEKER)
                .input(ItemTagRegistry.VS_SEEKERS)
                .input(Items.YELLOW_DYE)
                .criterion(FabricRecipeProvider.hasItem(VsSeekerItem.BLUE_VS_SEEKER), FabricRecipeProvider.conditionsFromItem(VsSeekerItem.BLUE_VS_SEEKER))
                .offerTo(exporter, String.format("%s_%s", getRecipeName(VsSeekerItem.YELLOW_VS_SEEKER), VS_SEEKER_RECIPE_SUFFIX));
    }
}
