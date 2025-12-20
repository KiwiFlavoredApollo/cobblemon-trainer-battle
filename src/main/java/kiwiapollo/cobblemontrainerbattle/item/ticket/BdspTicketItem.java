package kiwiapollo.cobblemontrainerbattle.item.ticket;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class BdspTicketItem {
    public static final List<Item> all = new ArrayList<>();

    public static final Item LEADER_ROARK_TICKET = register("leader_roark_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_roark")));
    public static final Item LEADER_GARDENIA_TICKET = register("leader_gardenia_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_gardenia")));
    public static final Item LEADER_MAYLENE_TICKET = register("leader_maylene_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_maylene")));
    public static final Item LEADER_CRASHER_WAKE_TICKET = register("leader_crasher_wake_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_crasher_wake")));
    public static final Item LEADER_FANTINA_TICKET = register("leader_fantina_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_fantina")));
    public static final Item LEADER_BYRON_TICKET = register("leader_byron_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_byron")));
    public static final Item LEADER_CANDICE_TICKET = register("leader_candice_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_candice")));
    public static final Item LEADER_VOLKNER_TICKET = register("leader_volkner_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_volkner")));
    public static final Item ELITE_AARON_TICKET = register("elite_aaron_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_aaron")));
    public static final Item ELITE_BERTHA_TICKET = register("elite_bertha_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_bertha")));
    public static final Item ELITE_FLINT_TICKET = register("elite_flint_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_flint")));
    public static final Item ELITE_LUCIAN_TICKET = register("elite_lucian_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_lucian")));
    public static final Item CHAMPION_CYNTHIA_TICKET = register("champion_cynthia_ticket", new TrainerTicket(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/champion_cynthia")));

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
