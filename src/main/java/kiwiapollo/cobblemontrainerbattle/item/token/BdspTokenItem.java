package kiwiapollo.cobblemontrainerbattle.item.token;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class BdspTokenItem {
    public static final List<Item> all = new ArrayList<>();

    public static final Item LEADER_ROARK_TOKEN = register("leader_roark_token", new TrainerToken());
    public static final Item LEADER_GARDENIA_TOKEN = register("leader_gardenia_token", new TrainerToken());
    public static final Item LEADER_MAYLENE_TOKEN = register("leader_maylene_token", new TrainerToken());
    public static final Item LEADER_CRASHER_WAKE_TOKEN = register("leader_crasher_wake_token", new TrainerToken());
    public static final Item LEADER_FANTINA_TOKEN = register("leader_fantina_token", new TrainerToken());
    public static final Item LEADER_BYRON_TOKEN = register("leader_byron_token", new TrainerToken());
    public static final Item LEADER_CANDICE_TOKEN = register("leader_candice_token", new TrainerToken());
    public static final Item LEADER_VOLKNER_TOKEN = register("leader_volkner_token", new TrainerToken());
    public static final Item ELITE_AARON_TOKEN = register("elite_aaron_token", new TrainerToken());
    public static final Item ELITE_BERTHA_TOKEN = register("elite_bertha_token", new TrainerToken());
    public static final Item ELITE_FLINT_TOKEN = register("elite_flint_token", new TrainerToken());
    public static final Item ELITE_LUCIAN_TOKEN = register("elite_lucian_token", new TrainerToken());
    public static final Item CHAMPION_CYNTHIA_TOKEN = register("champion_cynthia_token", new TrainerToken());

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
