package kiwiapollo.cobblemontrainerbattle.item.token;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public enum XyTokenItem {
    LEADER_VIOLA_TOKEN("leader_viola_token"),
    LEADER_GRANT_TOKEN("leader_grant_token"),
    LEADER_KORRINA_TOKEN("leader_korrina_token"),
    LEADER_RAMOS_TOKEN("leader_ramos_token"),
    LEADER_CLEMONT_TOKEN("leader_clemont_token"),
    LEADER_VALERIE_TOKEN("leader_valerie_token"),
    LEADER_OLYMPIA_TOKEN("leader_olympia_token"),
    LEADER_WULFRIC_TOKEN("leader_wulfric_token"),

    ELITE_WIKSTROM_TOKEN("elite_wikstrom_token"),
    ELITE_MALVA_TOKEN("elite_malva_token"),
    ELITE_DRASNA_TOKEN("elite_drasna_token"),
    ELITE_SIEBOLD_TOKEN("elite_siebold_token"),

    CHAMPION_DIANTHA_TOKEN("champion_diantha_token");

    private final Identifier identifier;
    private final Item item;

    XyTokenItem(String path) {
        this.identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, path);
        this.item = new TrainerToken();
    }

    public Item getItem() {
        return item;
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
