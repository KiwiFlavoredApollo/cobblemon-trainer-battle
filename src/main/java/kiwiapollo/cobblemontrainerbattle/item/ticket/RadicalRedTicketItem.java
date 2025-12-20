package kiwiapollo.cobblemontrainerbattle.item.ticket;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class RadicalRedTicketItem {
    public static final List<Item> all = new ArrayList<>();
    
    public static final Item LEADER_BROCK_TICKET = register("leader_brock_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_brock")));
    public static final Item LEADER_MISTY_TICKET = register("leader_misty_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_misty")));
    public static final Item LEADER_LT_SURGE_TICKET = register("leader_lt_surge_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_lt_surge")));
    public static final Item LEADER_ERIKA_TICKET = register("leader_erika_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_erika")));
    public static final Item LEADER_KOGA_TICKET = register("leader_koga_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_koga")));
    public static final Item LEADER_SABRINA_TICKET = register("leader_sabrina_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_sabrina")));
    public static final Item LEADER_BLAINE_TICKET = register("leader_blaine_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_blaine")));
    public static final Item LEADER_GIOVANNI_TICKET = register("leader_giovanni_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_giovanni")));
    public static final Item LEADER_FALKNER_TICKET = register("leader_falkner_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_falkner")));
    public static final Item LEADER_BUGSY_TICKET = register("leader_bugsy_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_bugsy")));
    public static final Item LEADER_WHITNEY_TICKET = register("leader_whitney_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_whitney")));
    public static final Item LEADER_MORTY_TICKET = register("leader_morty_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_morty")));
    public static final Item LEADER_CHUCK_TICKET = register("leader_chuck_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_chuck")));
    public static final Item LEADER_JASMINE_TICKET = register("leader_jasmine_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_jasmine")));
    public static final Item LEADER_PRYCE_TICKET = register("leader_pryce_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_pryce")));
    public static final Item LEADER_CLAIR_TICKET = register("leader_clair_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_clair")));
    public static final Item ELITE_LORELEI_TICKET = register("elite_lorelei_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_lorelei")));
    public static final Item ELITE_BRUNO_TICKET = register("elite_bruno_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_bruno")));
    public static final Item ELITE_AGATHA_TICKET = register("elite_agatha_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_agatha")));
    public static final Item ELITE_LANCE_TICKET = register("elite_lance_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_lance")));
    public static final Item CHAMPION_TERRY_TICKET = register("champion_terry_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/champion_terry")));

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
