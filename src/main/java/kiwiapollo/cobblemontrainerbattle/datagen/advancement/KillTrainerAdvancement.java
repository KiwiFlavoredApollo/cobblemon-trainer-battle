package kiwiapollo.cobblemontrainerbattle.datagen.advancement;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.KillTrainerCriterion;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public enum KillTrainerAdvancement implements CustomAdvancement {
    KILL_FIRST(Advancement.Builder.createUntelemetered()
            .parent(AdvancementProvider.ROOT)
            .criterion("kill_first_trainer", new KillTrainerCriterion.TotalCountConditions(1))
            .display(
                    Items.STONE_SWORD,
                    Text.translatable("advancement.cobblemontrainerbattle.kill_trainer.first.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.kill_trainer.first.description"),
                    AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "kill_first_trainer"))),

    KILL_TENTH(Advancement.Builder.createUntelemetered()
            .parent(KillTrainerAdvancement.KILL_FIRST.getAdvancement())
            .criterion("kill_tenth_trainer", new KillTrainerCriterion.TotalCountConditions(10))
            .display(
                    Items.IRON_SWORD,
                    Text.translatable("advancement.cobblemontrainerbattle.kill_trainer.tenth.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.kill_trainer.tenth.description"),
                    AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "kill_tenth_trainer")));

    private final Advancement advancement;

    KillTrainerAdvancement(Advancement advancement) {
        this.advancement = advancement;
    }

    @Override
    public Advancement getAdvancement() {
        return advancement;
    }
}
