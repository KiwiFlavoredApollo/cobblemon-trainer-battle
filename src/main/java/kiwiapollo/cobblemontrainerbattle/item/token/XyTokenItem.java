package kiwiapollo.cobblemontrainerbattle.item.token;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class XyTokenItem {
    public static final List<Item> all = new ArrayList<>();

    public static final Item LEADER_VIOLA_TOKEN = register("leader_viola_token", new TrainerToken());
    public static final Item LEADER_GRANT_TOKEN = register("leader_grant_token", new TrainerToken());
    public static final Item LEADER_KORRINA_TOKEN = register("leader_korrina_token", new TrainerToken());
    public static final Item LEADER_RAMOS_TOKEN = register("leader_ramos_token", new TrainerToken());
    public static final Item LEADER_CLEMONT_TOKEN = register("leader_clemont_token", new TrainerToken());
    public static final Item LEADER_VALERIE_TOKEN = register("leader_valerie_token", new TrainerToken());
    public static final Item LEADER_OLYMPIA_TOKEN = register("leader_olympia_token", new TrainerToken());
    public static final Item LEADER_WULFRIC_TOKEN = register("leader_wulfric_token", new TrainerToken());
    public static final Item ELITE_WIKSTROM_TOKEN = register("elite_wikstrom_token", new TrainerToken());
    public static final Item ELITE_MALVA_TOKEN = register("elite_malva_token", new TrainerToken());
    public static final Item ELITE_DRASNA_TOKEN = register("elite_drasna_token", new TrainerToken());
    public static final Item ELITE_SIEBOLD_TOKEN = register("elite_siebold_token", new TrainerToken());
    public static final Item CHAMPION_DIANTHA_TOKEN = register("champion_diantha_token", new TrainerToken());

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
