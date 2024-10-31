package kiwiapollo.cobblemontrainerbattle.datagen;

import net.minecraft.data.server.recipe.RecipeJsonProvider;

import java.util.function.Consumer;

public interface ItemRecipeGenerator {
    void generate(Consumer<RecipeJsonProvider> exporter);
}
