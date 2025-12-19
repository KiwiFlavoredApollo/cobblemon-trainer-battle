package kiwiapollo.cobblemontrainerbattle.block;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class CustomBlock {
    public static final List<Block> all = new ArrayList<>();

    public static final Block POKE_BALL_BOX = register("poke_ball_box", new PokeBallBox());

    public static void initialize() {

    }

    private static Block register(String name, Block block) {
        Identifier identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, name);
        Block registered = Registry.register(Registries.BLOCK, identifier, block);
        Registry.register(Registries.ITEM, identifier, new BlockItem(block, new Item.Settings()));
        all.add(registered);

        return registered;
    }

    public static List<Block> getAll() {
        return new ArrayList<>(all);
    }
}
