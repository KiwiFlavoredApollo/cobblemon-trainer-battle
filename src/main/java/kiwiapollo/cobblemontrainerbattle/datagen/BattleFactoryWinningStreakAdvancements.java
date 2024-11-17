package kiwiapollo.cobblemontrainerbattle.datagen;

import com.cobblemon.mod.common.CobblemonItems;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.BattleFactoryWinningStreakCriterion;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public enum BattleFactoryWinningStreakAdvancements implements CustomAdvancements {
    WINNING_STREAK_FIRST(Advancement.Builder.createUntelemetered()
            .parent(DataGenerator.AdvancementProvider.ROOT)
            .criterion("battlefactory_first_winning_streak", new BattleFactoryWinningStreakCriterion.Condition(1))
            .display(
                    CobblemonItems.RED_APRICORN,
                    Text.translatable("advancement.cobblemontrainerbattle.battlefactory_winning_streak.first.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.battlefactory_winning_streak.first.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "battlefactory_first_winning_streak"))),

    WINNING_STREAK_TENTH(Advancement.Builder.createUntelemetered()
            .parent(BattleFactoryWinningStreakAdvancements.WINNING_STREAK_FIRST.getAdvancement())
            .criterion("battlefactory_tenth_winning_streak", new BattleFactoryWinningStreakCriterion.Condition(10))
            .display(
                    CobblemonItems.BLUE_APRICORN,
                    Text.translatable("advancement.cobblemontrainerbattle.battlefactory_winning_streak.tenth.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.battlefactory_winning_streak.tenth.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "battlefactory_tenth_winning_streak")));

    private final Advancement advancement;

    BattleFactoryWinningStreakAdvancements(Advancement advancement) {
        this.advancement = advancement;
    }

    @Override
    public Advancement getAdvancement() {
        return advancement;
    }
}
