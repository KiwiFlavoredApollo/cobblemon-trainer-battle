package kiwiapollo.cobblemontrainerbattle.item.ticket;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class InclementEmeraldTicketItem {
    public static final List<Item> all = new ArrayList<>();
    
    public static final Item LEADER_ROXANNE_TICKET = register("leader_roxanne_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_roxanne")));
    public static final Item LEADER_BRAWLY_TICKET = register("leader_brawly_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_brawly")));
    public static final Item LEADER_WATTSON_TICKET = register("leader_wattson_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_wattson")));
    public static final Item LEADER_FLANNERY_TICKET = register("leader_flannery_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_flannery")));
    public static final Item LEADER_NORMAN_TICKET = register("leader_norman_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_norman")));
    public static final Item LEADER_WINONA_TICKET = register("leader_winona_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_winona")));
    public static final Item LEADER_TATE_AND_LIZA_TICKET = register("leader_tate_and_liza_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_tate_and_liza")));
    public static final Item LEADER_JUAN_TICKET = register("leader_juan_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_juan")));
    public static final Item ELITE_SIDNEY_TICKET = register("elite_sidney_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_sidney")));
    public static final Item ELITE_PHOEBE_TICKET = register("elite_phoebe_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_phoebe")));
    public static final Item ELITE_GLACIA_TICKET = register("elite_glacia_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_glacia")));
    public static final Item ELITE_DRAKE_TICKET = register("elite_drake_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_drake")));
    public static final Item CHAMPION_WALLACE_TICKET = register("champion_wallace_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/champion_wallace")));

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
