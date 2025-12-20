package kiwiapollo.cobblemontrainerbattle.item.token;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class RadicalRedTokenItem {
    public static final List<Item> all = new ArrayList<>();

    public static final Item LEADER_BROCK_TOKEN = register("leader_brock_token", new TrainerToken());
    public static final Item LEADER_MISTY_TOKEN = register("leader_misty_token", new TrainerToken());
    public static final Item LEADER_LT_SURGE_TOKEN = register("leader_lt_surge_token", new TrainerToken());
    public static final Item LEADER_ERIKA_TOKEN = register("leader_erika_token", new TrainerToken());
    public static final Item LEADER_KOGA_TOKEN = register("leader_koga_token", new TrainerToken());
    public static final Item LEADER_SABRINA_TOKEN = register("leader_sabrina_token", new TrainerToken());
    public static final Item LEADER_BLAINE_TOKEN = register("leader_blaine_token", new TrainerToken());
    public static final Item LEADER_GIOVANNI_TOKEN = register("leader_giovanni_token", new TrainerToken());
    public static final Item LEADER_FALKNER_TOKEN = register("leader_falkner_token", new TrainerToken());
    public static final Item LEADER_BUGSY_TOKEN = register("leader_bugsy_token", new TrainerToken());
    public static final Item LEADER_WHITNEY_TOKEN = register("leader_whitney_token", new TrainerToken());
    public static final Item LEADER_MORTY_TOKEN = register("leader_morty_token", new TrainerToken());
    public static final Item LEADER_CHUCK_TOKEN = register("leader_chuck_token", new TrainerToken());
    public static final Item LEADER_JASMINE_TOKEN = register("leader_jasmine_token", new TrainerToken());
    public static final Item LEADER_PRYCE_TOKEN = register("leader_pryce_token", new TrainerToken());
    public static final Item LEADER_CLAIR_TOKEN = register("leader_clair_token", new TrainerToken());
    public static final Item ELITE_LORELEI_TOKEN = register("elite_lorelei_token", new TrainerToken());
    public static final Item ELITE_BRUNO_TOKEN = register("elite_bruno_token", new TrainerToken());
    public static final Item ELITE_AGATHA_TOKEN = register("elite_agatha_token", new TrainerToken());
    public static final Item ELITE_LANCE_TOKEN = register("elite_lance_token", new TrainerToken());
    public static final Item CHAMPION_TERRY_TOKEN = register("champion_terry_token", new TrainerToken());

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
