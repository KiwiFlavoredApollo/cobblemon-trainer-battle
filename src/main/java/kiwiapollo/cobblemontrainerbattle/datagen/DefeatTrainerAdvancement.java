package kiwiapollo.cobblemontrainerbattle.datagen;

import com.cobblemon.mod.common.CobblemonItems;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.DefeatTrainerCriterion;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public enum DefeatTrainerAdvancement implements CustomAdvancement {
    DEFEAT_FIRST(Advancement.Builder.createUntelemetered()
            .parent(DataGenerator.AdvancementProvider.ROOT)
            .criterion("defeat_first_trainer", new DefeatTrainerCriterion.TotalCountConditions(1))
            .display(
                    CobblemonItems.POKE_BALL,
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_trainer.first.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_trainer.first.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_first_trainer"))),

    DEFEAT_TENTH(Advancement.Builder.createUntelemetered()
            .parent(DefeatTrainerAdvancement.DEFEAT_FIRST.getAdvancement())
            .criterion("defeat_tenth_trainer", new DefeatTrainerCriterion.TotalCountConditions(10))
            .display(
                    CobblemonItems.GREAT_BALL,
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_trainer.tenth.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_trainer.tenth.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_tenth_trainer")));

    private final Advancement advancement;

    DefeatTrainerAdvancement(Advancement advancement) {
        this.advancement = advancement;
    }

    @Override
    public Advancement getAdvancement() {
        return advancement;
    }
}
