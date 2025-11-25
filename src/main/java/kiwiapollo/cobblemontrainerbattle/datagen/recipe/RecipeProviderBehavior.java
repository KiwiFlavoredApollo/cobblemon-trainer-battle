package kiwiapollo.cobblemontrainerbattle.datagen.recipe;

import net.minecraft.data.server.recipe.RecipeJsonProvider;

import java.util.function.Consumer;

public interface RecipeProviderBehavior {
    void generate(Consumer<RecipeJsonProvider> exporter);
}
