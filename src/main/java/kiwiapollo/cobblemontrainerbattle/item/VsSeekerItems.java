package kiwiapollo.cobblemontrainerbattle.item;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.function.Predicate;

public enum VsSeekerItems {
    BLUE_VS_SEEKER("blue_vs_seeker", new VsSeeker(trainer -> true)),
    RED_VS_SEEKER("red_vs_seeker", new VsSeeker(new SeriesPredicate("radicalred"))),
    GREEN_VS_SEEKER("green_vs_seeker", new VsSeeker(new SeriesPredicate("inclementemerald"))),
    PURPLE_VS_SEEKER("purple_vs_seeker", new VsSeeker(new SeriesPredicate("smogon"))),
    PINK_VS_SEEKER("pink_vs_seeker", new VsSeeker(new SeriesPredicate("xy"))),
    YELLOW_VS_SEEKER("yellow_vs_seeker", new VsSeeker(new SeriesPredicate("bdsp")));

    private final Identifier identifier;
    private final Item item;

    VsSeekerItems(String path, Item item) {
        this.identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, path);
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    private static class SeriesPredicate implements Predicate<String> {
        private final String series;

        SeriesPredicate(String series) {
            this.series = series;
        }

        @Override
        public boolean test(String trainer) {
            return trainer.matches(String.format("^%s/.+", series));
        }
    }
}
