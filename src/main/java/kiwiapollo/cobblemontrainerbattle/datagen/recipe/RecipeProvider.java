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
        new VsSeekerRecipeProvider().generate(exporter);
        new InclementEmeraldTicketRecipeProvider().generate(exporter);
        new RadicalRedTicketRecipeProvider().generate(exporter);
        new XyTicketRecipeProvider().generate(exporter);
        new BdspTicketRecipeProvider().generate(exporter);
    }
}