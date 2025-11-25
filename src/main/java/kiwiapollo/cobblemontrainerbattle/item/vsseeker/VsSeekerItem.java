package kiwiapollo.cobblemontrainerbattle.item.vsseeker;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.function.Predicate;

public enum VsSeekerItem {
    BLUE_VS_SEEKER("blue_vs_seeker", new BlueVsSeeker()),
    RED_VS_SEEKER("red_vs_seeker", new RedVsSeeker()),
    GREEN_VS_SEEKER("green_vs_seeker", new GreenVsSeeker()),
    PURPLE_VS_SEEKER("purple_vs_seeker", new PurpleVsSeeker()),
    PINK_VS_SEEKER("pink_vs_seeker", new PinkVsSeeker()),
    YELLOW_VS_SEEKER("yellow_vs_seeker", new YellowVsSeeker());

    private final Identifier identifier;
    private final Item item;

    VsSeekerItem(String path, Item item) {
        this.identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, path);
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
