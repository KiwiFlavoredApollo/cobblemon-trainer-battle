
package kiwiapollo.cobblemontrainerbattle.item.ticket;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public enum XyTicketItem {
    LEADER_VIOLA_TICKET("leader_viola_ticket", "entity/leader_viola"),
    LEADER_GRANT_TICKET("leader_grant_ticket", "entity/leader_grant"),
    LEADER_KORRINA_TICKET("leader_korrina_ticket", "entity/leader_korrina"),
    LEADER_RAMOS_TICKET("leader_ramos_ticket", "entity/leader_ramos"),
    LEADER_CLEMONT_TICKET("leader_clemont_ticket", "entity/leader_clemont"),
    LEADER_VALERIE_TICKET("leader_valerie_ticket", "entity/leader_valerie"),
    LEADER_OLYMPIA_TICKET("leader_olympia_ticket", "entity/leader_olympia"),
    LEADER_WULFRIC_TICKET("leader_wulfric_ticket", "entity/leader_wulfric"),

    ELITE_WIKSTROM_TICKET("elite_wikstrom_ticket", "entity/elite_wikstrom"),
    ELITE_MALVA_TICKET("elite_malva_ticket", "entity/elite_malva"),
    ELITE_DRASNA_TICKET("elite_drasna_ticket", "entity/elite_drasna"),
    ELITE_SIEBOLD_TICKET("elite_siebold_ticket", "entity/elite_siebold"),

    CHAMPION_DIANTHA_TICKET("champion_diantha_ticket", "entity/champion_diantha");

    private final Identifier identifier;
    private final TrainerTicket item;

    XyTicketItem(String path, String trainer) {
        this.identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, path);
        this.item = new TrainerTicket(new Item.Settings(), Identifier.of(CobblemonTrainerBattle.MOD_ID, trainer));
    }

    public Item getItem() {
        return item;
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
