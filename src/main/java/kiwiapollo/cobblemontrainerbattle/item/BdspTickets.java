package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public enum BdspTickets {
    LEADER_ROARK_TICKET("leader_roark_ticket", "entity/leader_roark"),
    LEADER_GARDENIA_TICKET("leader_gardenia_ticket", "entity/leader_gardenia"),
    LEADER_MAYLENE_TICKET("leader_maylene_ticket", "entity/leader_maylene"),
    LEADER_CRASHER_WAKE_TICKET("leader_crasher_wake_ticket", "entity/leader_crasher_wake"),
    LEADER_FANTINA_TICKET("leader_fantina_ticket", "entity/leader_fantina"),
    LEADER_BYRON_TICKET("leader_byron_ticket", "entity/leader_byron"),
    LEADER_CANDICE_TICKET("leader_candice_ticket", "entity/leader_candice"),
    LEADER_VOLKNER_TICKET("leader_volkner_ticket", "entity/leader_volkner"),

    ELITE_AARON_TICKET("elite_aaron_ticket", "entity/elite_aaron"),
    ELITE_BERTHA_TICKET("elite_bertha_ticket", "entity/elite_bertha"),
    ELITE_FLINT_TICKET("elite_flint_ticket", "entity/elite_flint"),
    ELITE_LUCIAN_TICKET("elite_lucian_ticket", "entity/elite_lucian"),

    CHAMPION_CYNTHIA_TICKET("champion_cynthia_ticket", "entity/champion_cynthia");

    private final Identifier identifier;
    private final TrainerTicket item;

    BdspTickets(String path, String trainer) {
        this.identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, path);
        this.item = new TrainerTicket(new Item.Settings(), trainer);
    }

    public Item getItem() {
        return item;
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
