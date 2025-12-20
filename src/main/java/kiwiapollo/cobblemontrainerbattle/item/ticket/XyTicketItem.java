
package kiwiapollo.cobblemontrainerbattle.item.ticket;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class XyTicketItem {
    public static final List<Item> all = new ArrayList<>();

    public static final Item LEADER_VIOLA_TICKET = register("leader_viola_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_viola")));
    public static final Item LEADER_GRANT_TICKET = register("leader_grant_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_grant")));
    public static final Item LEADER_KORRINA_TICKET = register("leader_korrina_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_korrina")));
    public static final Item LEADER_RAMOS_TICKET = register("leader_ramos_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_ramos")));
    public static final Item LEADER_CLEMONT_TICKET = register("leader_clemont_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_clemont")));
    public static final Item LEADER_VALERIE_TICKET = register("leader_valerie_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_valerie")));
    public static final Item LEADER_OLYMPIA_TICKET = register("leader_olympia_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_olympia")));
    public static final Item LEADER_WULFRIC_TICKET = register("leader_wulfric_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_wulfric")));
    public static final Item ELITE_WIKSTROM_TICKET = register("elite_wikstrom_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_wikstrom")));
    public static final Item ELITE_MALVA_TICKET = register("elite_malva_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_malva")));
    public static final Item ELITE_DRASNA_TICKET = register("elite_drasna_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_drasna")));
    public static final Item ELITE_SIEBOLD_TICKET = register("elite_siebold_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_siebold")));
    public static final Item CHAMPION_DIANTHA_TICKET = register("champion_diantha_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/champion_diantha")));

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
