package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public enum BdspTokens {
    LEADER_ROARK_TOKEN("leader_roark_token"),
    LEADER_GARDENIA_TOKEN("leader_gardenia_token"),
    LEADER_MAYLENE_TOKEN("leader_maylene_token"),
    LEADER_CRASHER_WAKE_TOKEN("leader_crasher_wake_token"),
    LEADER_FANTINA_TOKEN("leader_fantina_token"),
    LEADER_BYRON_TOKEN("leader_byron_token"),
    LEADER_CANDICE_TOKEN("leader_candice_token"),
    LEADER_VOLKNER_TOKEN("leader_volkner_token"),

    ELITE_AARON_TOKEN("elite_aaron_token"),
    ELITE_BERTHA_TOKEN("elite_bertha_token"),
    ELITE_FLINT_TOKEN("elite_flint_token"),
    ELITE_LUCIAN_TOKEN("elite_lucian_token"),

    CHAMPION_CYNTHIA_TOKEN("champion_cynthia_token");

    private final Identifier identifier;
    private final Item item;

    BdspTokens(String path) {
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
