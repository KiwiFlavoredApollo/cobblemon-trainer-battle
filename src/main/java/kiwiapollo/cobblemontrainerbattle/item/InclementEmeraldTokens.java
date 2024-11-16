package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public enum InclementEmeraldTokens {
    LEADER_ROXANNE_TOKEN("leader_roxanne_token"),
    LEADER_BRAWLY_TOKEN("leader_brawly_token"),
    LEADER_WATTSON_TOKEN("leader_wattson_token"),
    LEADER_FLANNERY_TOKEN("leader_flannery_token"),
    LEADER_NORMAN_TOKEN("leader_norman_token"),
    LEADER_WINONA_TOKEN("leader_winona_token"),
    LEADER_TATE_AND_LIZA_TOKEN("leader_tate_and_liza_token"),
    LEADER_JUAN_TOKEN("leader_juan_token"),

    ELITE_SIDNEY_TOKEN("elite_sidney_token"),
    ELITE_PHOEBE_TOKEN("elite_phoebe_token"),
    ELITE_GLACIA_TOKEN("elite_glacia_token"),
    ELITE_DRAKE_TOKEN("elite_drake_token"),

    CHAMPION_WALLACE_TOKEN("champion_wallace_token");

    private final Identifier identifier;
    private final Item item;

    InclementEmeraldTokens(String path) {
        this.identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, path);
        this.item = new Item(new Item.Settings());
    }

    public Item getItem() {
        return item;
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
