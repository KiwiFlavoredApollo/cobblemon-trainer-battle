package kiwiapollo.cobblemontrainerbattle.datagen;

import kiwiapollo.cobblemontrainerbattle.datagen.advancement.AdvancementProvider;
import kiwiapollo.cobblemontrainerbattle.datagen.loottable.BlockLootTableProvider;
import kiwiapollo.cobblemontrainerbattle.datagen.loottable.TrainerEntityLootTableProvider;
import kiwiapollo.cobblemontrainerbattle.datagen.recipe.RecipeProvider;
import kiwiapollo.cobblemontrainerbattle.datagen.tag.BlockTagProvider;
import kiwiapollo.cobblemontrainerbattle.datagen.tag.PoiTagProvider;
import kiwiapollo.cobblemontrainerbattle.datagen.tag.ItemTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

@SuppressWarnings("unused")
public class DataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(AdvancementProvider::new);
        pack.addProvider(RecipeProvider::new);
        pack.addProvider(ItemTagProvider::new);
        pack.addProvider(PoiTagProvider::new);
        pack.addProvider(BlockTagProvider::new);
        pack.addProvider(TrainerEntityLootTableProvider::new);
        pack.addProvider(BlockLootTableProvider::new);
    }
}
