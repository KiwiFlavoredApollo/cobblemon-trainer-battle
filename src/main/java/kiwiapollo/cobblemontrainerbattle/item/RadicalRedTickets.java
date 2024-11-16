package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.entity.RadicalRedTrainerEntityPresetRegistry;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntityPreset;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public enum RadicalRedTickets {
    LEADER_BROCK_TICKET("leader_brock_ticket", RadicalRedTrainerEntityPresetRegistry.LEADER_BROCK),
    LEADER_MISTY_TICKET("leader_misty_ticket", RadicalRedTrainerEntityPresetRegistry.LEADER_MISTY),
    LEADER_LT_SURGE_TICKET("leader_lt_surge_ticket", RadicalRedTrainerEntityPresetRegistry.LEADER_LT_SURGE),
    LEADER_ERIKA_TICKET("leader_erika_ticket", RadicalRedTrainerEntityPresetRegistry.LEADER_ERIKA),
    LEADER_KOGA_TICKET("leader_koga_ticket", RadicalRedTrainerEntityPresetRegistry.LEADER_KOGA),
    LEADER_SABRINA_TICKET("leader_sabrina_ticket", RadicalRedTrainerEntityPresetRegistry.LEADER_SABRINA),
    LEADER_BLAINE_TICKET("leader_blaine_ticket", RadicalRedTrainerEntityPresetRegistry.LEADER_BLAINE),
    LEADER_GIOVANNI_TICKET("leader_giovanni_ticket", RadicalRedTrainerEntityPresetRegistry.LEADER_GIOVANNI),

    LEADER_FALKNER_TICKET("leader_falkner_ticket", RadicalRedTrainerEntityPresetRegistry.LEADER_FALKNER),
    LEADER_BUGSY_TICKET("leader_bugsy_ticket", RadicalRedTrainerEntityPresetRegistry.LEADER_BUGSY),
    LEADER_WHITNEY_TICKET("leader_whitney_ticket", RadicalRedTrainerEntityPresetRegistry.LEADER_WHITNEY),
    LEADER_MORTY_TICKET("leader_morty_ticket", RadicalRedTrainerEntityPresetRegistry.LEADER_MORTY),
    LEADER_CHUCK_TICKET("leader_chuck_ticket", RadicalRedTrainerEntityPresetRegistry.LEADER_CHUCK),
    LEADER_JASMINE_TICKET("leader_jasmine_ticket", RadicalRedTrainerEntityPresetRegistry.LEADER_JASMINE),
    LEADER_PRYCE_TICKET("leader_pryce_ticket", RadicalRedTrainerEntityPresetRegistry.LEADER_PRYCE),
    LEADER_CLAIR_TICKET("leader_clair_ticket", RadicalRedTrainerEntityPresetRegistry.LEADER_CLAIR),

    ELITE_LORELEI_TICKET("elite_lorelei_ticket", RadicalRedTrainerEntityPresetRegistry.ELITE_LORELEI),
    ELITE_BRUNO_TICKET("elite_bruno_ticket", RadicalRedTrainerEntityPresetRegistry.ELITE_BRUNO),
    ELITE_AGATHA_TICKET("elite_agatha_ticket", RadicalRedTrainerEntityPresetRegistry.ELITE_AGATHA),
    ELITE_LANCE_TICKET("elite_lance_ticket", RadicalRedTrainerEntityPresetRegistry.ELITE_LANCE),

    CHAMPION_TERRY_TICKET("champion_terry_ticket", RadicalRedTrainerEntityPresetRegistry.CHAMPION_TERRY);

    private final Identifier identifier;
    private final TrainerTicket item;

    RadicalRedTickets(String path, TrainerEntityPreset preset) {
        this.identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, path);
        this.item = new TrainerTicket(new Item.Settings(), preset);
    }

    public Item getItem() {
        return item;
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
