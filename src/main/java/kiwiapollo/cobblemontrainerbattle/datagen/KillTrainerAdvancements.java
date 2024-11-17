package kiwiapollo.cobblemontrainerbattle.datagen;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.KillTrainerCriterion;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public enum KillTrainerAdvancements implements CustomAdvancements {
    KILL_FIRST(Advancement.Builder.createUntelemetered()
            .parent(DataGenerator.AdvancementProvider.ROOT)
            .criterion("kill_first_trainer", new KillTrainerCriterion.TotalCountConditions(1))
            .display(
                    Items.STONE_SWORD,
                    Text.translatable("advancement.cobblemontrainerbattle.kill_trainer.first.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.kill_trainer.first.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "kill_first_trainer"))),

    KILL_TENTH(Advancement.Builder.createUntelemetered()
            .parent(KillTrainerAdvancements.KILL_FIRST.getAdvancement())
            .criterion("kill_tenth_trainer", new KillTrainerCriterion.TotalCountConditions(10))
            .display(
                    Items.IRON_SWORD,
                    Text.translatable("advancement.cobblemontrainerbattle.kill_trainer.tenth.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.kill_trainer.tenth.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "kill_tenth_trainer")));

    private final Advancement advancement;

    KillTrainerAdvancements(Advancement advancement) {
        this.advancement = advancement;
    }

    @Override
    public Advancement getAdvancement() {
        return advancement;
    }
}
