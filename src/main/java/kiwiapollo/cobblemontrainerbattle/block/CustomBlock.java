package kiwiapollo.cobblemontrainerbattle.block;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public enum CustomBlock {
    POKE_BALL_BOX(new PokeBallBox(), "poke_ball_box");

    private final Block block;
    private final Identifier identifier;
    private final BlockItem item;

    CustomBlock(Block block, String id) {
        this.block = block;
        this.item = new BlockItem(block, new Item.Settings());
        this.identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, id);
    }

    public Block getBlock() {
        return block;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public BlockItem getItem() {
        return item;
    }
}
