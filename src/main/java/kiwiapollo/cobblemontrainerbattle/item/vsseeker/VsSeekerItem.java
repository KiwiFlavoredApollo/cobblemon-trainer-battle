package kiwiapollo.cobblemontrainerbattle.item.vsseeker;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class VsSeekerItem {
    public static final List<Item> all = new ArrayList<>();

    public static final Item BLUE_VS_SEEKER = register("blue_vs_seeker", new BlueVsSeeker());
    public static final Item RED_VS_SEEKER = register("red_vs_seeker", new RedVsSeeker());
    public static final Item GREEN_VS_SEEKER = register("green_vs_seeker", new GreenVsSeeker());
    public static final Item PURPLE_VS_SEEKER = register("purple_vs_seeker", new PurpleVsSeeker());
    public static final Item PINK_VS_SEEKER = register("pink_vs_seeker", new PinkVsSeeker());
    public static final Item YELLOW_VS_SEEKER = register("yellow_vs_seeker", new YellowVsSeeker());

    public static void initialize() {

    }

    private static Item register(String name, Item item) {
        Identifier identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, name);
        Item registered = Registry.register(Registries.ITEM, identifier, item);
        all.add(registered);

        return registered;
    }

    public static List<Item> getAll() {
        return new ArrayList<>(all);
    }
}
