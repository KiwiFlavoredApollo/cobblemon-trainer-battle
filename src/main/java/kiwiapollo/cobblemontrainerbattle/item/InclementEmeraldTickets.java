package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.entity.InclementEmeraldTrainerEntityPresetRegistry;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntityPreset;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public enum InclementEmeraldTickets {
    LEADER_ROXANNE_TICKET("leader_roxanne_ticket", InclementEmeraldTrainerEntityPresetRegistry.LEADER_ROXANNE),
    LEADER_BRAWLY_TICKET("leader_brawly_ticket", InclementEmeraldTrainerEntityPresetRegistry.LEADER_BRAWLY),
    LEADER_WATTSON_TICKET("leader_wattson_ticket", InclementEmeraldTrainerEntityPresetRegistry.LEADER_WATTSON),
    LEADER_FLANNERY_TICKET("leader_flannery_ticket", InclementEmeraldTrainerEntityPresetRegistry.LEADER_FLANNERY),
    LEADER_NORMAN_TICKET("leader_norman_ticket", InclementEmeraldTrainerEntityPresetRegistry.LEADER_NORMAN),
    LEADER_WINONA_TICKET("leader_winona_ticket", InclementEmeraldTrainerEntityPresetRegistry.LEADER_WINONA),
    LEADER_TATE_AND_LIZA_TICKET("leader_tate_and_liza_ticket", InclementEmeraldTrainerEntityPresetRegistry.LEADER_TATE_AND_LIZA),
    LEADER_JUAN_TICKET("leader_juan_ticket", InclementEmeraldTrainerEntityPresetRegistry.LEADER_JUAN),

    ELITE_SIDNEY_TICKET("elite_sidney_ticket", InclementEmeraldTrainerEntityPresetRegistry.ELITE_SIDNEY),
    ELITE_PHOEBE_TICKET("elite_phoebe_ticket", InclementEmeraldTrainerEntityPresetRegistry.ELITE_PHOEBE),
    ELITE_GLACIA_TICKET("elite_glacia_ticket", InclementEmeraldTrainerEntityPresetRegistry.ELITE_GLACIA),
    ELITE_DRAKE_TICKET("elite_drake_ticket", InclementEmeraldTrainerEntityPresetRegistry.ELITE_DRAKE),

    CHAMPION_WALLACE_TICKET("champion_wallace_ticket", InclementEmeraldTrainerEntityPresetRegistry.CHAMPION_WALLACE);

    private final Identifier identifier;
    private final TrainerTicket item;

    InclementEmeraldTickets(String path, TrainerEntityPreset preset) {
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
