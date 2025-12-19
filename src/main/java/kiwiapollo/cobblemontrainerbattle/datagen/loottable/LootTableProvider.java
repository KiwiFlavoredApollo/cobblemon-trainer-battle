package kiwiapollo.cobblemontrainerbattle.datagen.loottable;

import kiwiapollo.cobblemontrainerbattle.block.CustomBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class LootTableProvider extends FabricBlockLootTableProvider {
    public LootTableProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate() {
        addDrop(CustomBlock.POKE_BALL_BOX, drops(CustomBlock.POKE_BALL_BOX));
    }
}
