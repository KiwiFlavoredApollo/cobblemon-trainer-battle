package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.function.Predicate;

public enum VsSeekerItem {
    BLUE_VS_SEEKER("blue_vs_seeker", new VsSeeker("item.cobblemontrainerbattle.blue_vs_seeker.trainers", trainer -> true)),
    RED_VS_SEEKER("red_vs_seeker", new VsSeeker("item.cobblemontrainerbattle.red_vs_seeker.trainers", new GroupPredicate("radicalred"))),
    GREEN_VS_SEEKER("green_vs_seeker", new VsSeeker("item.cobblemontrainerbattle.green_vs_seeker.trainers", new GroupPredicate("inclementemerald"))),
    PURPLE_VS_SEEKER("purple_vs_seeker", new VsSeeker("item.cobblemontrainerbattle.purple_vs_seeker.trainers", new GroupPredicate("smogon"))),
    PINK_VS_SEEKER("pink_vs_seeker", new VsSeeker("item.cobblemontrainerbattle.pink_vs_seeker.trainers", new GroupPredicate("xy"))),
    YELLOW_VS_SEEKER("yellow_vs_seeker", new VsSeeker("item.cobblemontrainerbattle.yellow_vs_seeker.trainers", new GroupPredicate("bdsp")));

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

    private static class GroupPredicate implements Predicate<String> {
        private final String group;

        GroupPredicate(String group) {
            this.group = group;
        }

        @Override
        public boolean test(String trainer) {
            return trainer.matches(String.format("^%s/.+", group));
        }
    }
}
