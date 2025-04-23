package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public enum InclementEmeraldTicketItems {
    LEADER_ROXANNE_TICKET("leader_roxanne_ticket", "entity/leader_roxanne"),
    LEADER_BRAWLY_TICKET("leader_brawly_ticket", "entity/leader_brawly"),
    LEADER_WATTSON_TICKET("leader_wattson_ticket", "entity/leader_wattson"),
    LEADER_FLANNERY_TICKET("leader_flannery_ticket", "entity/leader_flannery"),
    LEADER_NORMAN_TICKET("leader_norman_ticket", "entity/leader_norman"),
    LEADER_WINONA_TICKET("leader_winona_ticket", "entity/leader_winona"),
    LEADER_TATE_AND_LIZA_TICKET("leader_tate_and_liza_ticket", "entity/leader_tate_and_liza"),
    LEADER_JUAN_TICKET("leader_juan_ticket", "entity/leader_juan"),

    ELITE_SIDNEY_TICKET("elite_sidney_ticket", "entity/elite_sidney"),
    ELITE_PHOEBE_TICKET("elite_phoebe_ticket", "entity/elite_phoebe"),
    ELITE_GLACIA_TICKET("elite_glacia_ticket", "entity/elite_glacia"),
    ELITE_DRAKE_TICKET("elite_drake_ticket", "entity/elite_drake"),

    CHAMPION_WALLACE_TICKET("champion_wallace_ticket", "entity/champion_wallace");

    private final Identifier identifier;
    private final TrainerTicket item;

    InclementEmeraldTicketItems(String path, String trainer) {
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
