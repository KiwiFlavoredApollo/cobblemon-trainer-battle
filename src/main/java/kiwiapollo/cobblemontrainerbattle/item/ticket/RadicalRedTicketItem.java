package kiwiapollo.cobblemontrainerbattle.item.ticket;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public enum RadicalRedTicketItem {
    LEADER_BROCK_TICKET("leader_brock_ticket", "entity/leader_brock"),
    LEADER_MISTY_TICKET("leader_misty_ticket", "entity/leader_misty"),
    LEADER_LT_SURGE_TICKET("leader_lt_surge_ticket", "entity/leader_lt_surge"),
    LEADER_ERIKA_TICKET("leader_erika_ticket", "entity/leader_erika"),
    LEADER_KOGA_TICKET("leader_koga_ticket", "entity/leader_koga"),
    LEADER_SABRINA_TICKET("leader_sabrina_ticket", "entity/leader_sabrina"),
    LEADER_BLAINE_TICKET("leader_blaine_ticket", "entity/leader_blaine"),
    LEADER_GIOVANNI_TICKET("leader_giovanni_ticket", "entity/leader_giovanni"),

    LEADER_FALKNER_TICKET("leader_falkner_ticket", "entity/leader_falkner"),
    LEADER_BUGSY_TICKET("leader_bugsy_ticket", "entity/leader_bugsy"),
    LEADER_WHITNEY_TICKET("leader_whitney_ticket", "entity/leader_whitney"),
    LEADER_MORTY_TICKET("leader_morty_ticket", "entity/leader_morty"),
    LEADER_CHUCK_TICKET("leader_chuck_ticket", "entity/leader_chuck"),
    LEADER_JASMINE_TICKET("leader_jasmine_ticket", "entity/leader_jasmine"),
    LEADER_PRYCE_TICKET("leader_pryce_ticket", "entity/leader_pryce"),
    LEADER_CLAIR_TICKET("leader_clair_ticket", "entity/leader_clair"),

    ELITE_LORELEI_TICKET("elite_lorelei_ticket", "entity/elite_lorelei"),
    ELITE_BRUNO_TICKET("elite_bruno_ticket", "entity/elite_bruno"),
    ELITE_AGATHA_TICKET("elite_agatha_ticket", "entity/elite_agatha"),
    ELITE_LANCE_TICKET("elite_lance_ticket", "entity/elite_lance"),

    CHAMPION_TERRY_TICKET("champion_terry_ticket", "entity/champion_terry");

    private final Identifier identifier;
    private final TrainerTicket item;

    RadicalRedTicketItem(String path, String trainer) {
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
