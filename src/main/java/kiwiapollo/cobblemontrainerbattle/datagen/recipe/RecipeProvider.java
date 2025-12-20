package kiwiapollo.cobblemontrainerbattle.datagen.recipe;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;

import java.util.function.Consumer;

public class RecipeProvider extends FabricRecipeProvider {
    public RecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        new VsSeekerRecipeProvider(output).generate(exporter);
        new InclementEmeraldTicketRecipeProvider(output).generate(exporter);
        new RadicalRedTicketRecipeProvider(output).generate(exporter);
        new XyTicketRecipeProvider(output).generate(exporter);
        new BdspTicketRecipeProvider(output).generate(exporter);
    }
}