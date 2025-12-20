package kiwiapollo.cobblemontrainerbattle.item.token;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class InclementEmeraldTokenItem {
    public static final List<Item> all = new ArrayList<>();

    public static final Item LEADER_ROXANNE_TOKEN = register("leader_roxanne_token", new TrainerToken());
    public static final Item LEADER_BRAWLY_TOKEN = register("leader_brawly_token", new TrainerToken());
    public static final Item LEADER_WATTSON_TOKEN = register("leader_wattson_token", new TrainerToken());
    public static final Item LEADER_FLANNERY_TOKEN = register("leader_flannery_token", new TrainerToken());
    public static final Item LEADER_NORMAN_TOKEN = register("leader_norman_token", new TrainerToken());
    public static final Item LEADER_WINONA_TOKEN = register("leader_winona_token", new TrainerToken());
    public static final Item LEADER_TATE_AND_LIZA_TOKEN = register("leader_tate_and_liza_token", new TrainerToken());
    public static final Item LEADER_JUAN_TOKEN = register("leader_juan_token", new TrainerToken());
    public static final Item ELITE_SIDNEY_TOKEN = register("elite_sidney_token", new TrainerToken());
    public static final Item ELITE_PHOEBE_TOKEN = register("elite_phoebe_token", new TrainerToken());
    public static final Item ELITE_GLACIA_TOKEN = register("elite_glacia_token", new TrainerToken());
    public static final Item ELITE_DRAKE_TOKEN = register("elite_drake_token", new TrainerToken());
    public static final Item CHAMPION_WALLACE_TOKEN = register("champion_wallace_token", new TrainerToken());

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
