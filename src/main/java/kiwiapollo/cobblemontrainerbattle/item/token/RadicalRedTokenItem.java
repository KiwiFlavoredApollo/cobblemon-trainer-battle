package kiwiapollo.cobblemontrainerbattle.item.token;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public enum RadicalRedTokenItem {
    LEADER_BROCK_TOKEN("leader_brock_token"),
    LEADER_MISTY_TOKEN("leader_misty_token"),
    LEADER_LT_SURGE_TOKEN("leader_lt_surge_token"),
    LEADER_ERIKA_TOKEN("leader_erika_token"),
    LEADER_KOGA_TOKEN("leader_koga_token"),
    LEADER_SABRINA_TOKEN("leader_sabrina_token"),
    LEADER_BLAINE_TOKEN("leader_blaine_token"),
    LEADER_GIOVANNI_TOKEN("leader_giovanni_token"),

    LEADER_FALKNER_TOKEN("leader_falkner_token"),
    LEADER_BUGSY_TOKEN("leader_bugsy_token"),
    LEADER_WHITNEY_TOKEN("leader_whitney_token"),
    LEADER_MORTY_TOKEN("leader_morty_token"),
    LEADER_CHUCK_TOKEN("leader_chuck_token"),
    LEADER_JASMINE_TOKEN("leader_jasmine_token"),
    LEADER_PRYCE_TOKEN("leader_pryce_token"),
    LEADER_CLAIR_TOKEN("leader_clair_token"),

    ELITE_LORELEI_TOKEN("elite_lorelei_token"),
    ELITE_BRUNO_TOKEN("elite_bruno_token"),
    ELITE_AGATHA_TOKEN("elite_agatha_token"),
    ELITE_LANCE_TOKEN("elite_lance_token"),

    CHAMPION_TERRY_TOKEN("champion_terry_token");

    private final Identifier identifier;
    private final Item item;

    RadicalRedTokenItem(String path) {
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
