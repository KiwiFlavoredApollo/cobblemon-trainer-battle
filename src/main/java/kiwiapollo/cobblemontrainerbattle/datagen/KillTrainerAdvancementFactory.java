package kiwiapollo.cobblemontrainerbattle.datagen;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.KillTrainerCriterion;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class KillTrainerAdvancementFactory implements AdvancementFactory {
    private static final Advancement FIRST = Advancement.Builder.createUntelemetered()
            .parent(DataGenerator.AdvancementProvider.ROOT)
            .criterion("kill_first_trainer", new KillTrainerCriterion.Conditions(1))
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "kill_first_trainer"));

    private static final Advancement TENTH = Advancement.Builder.createUntelemetered()
            .parent(FIRST)
            .criterion("kill_tenth_trainer", new KillTrainerCriterion.Conditions(10))
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "kill_tenth_trainer"));

    public KillTrainerAdvancementFactory() {

    }

    @Override
    public List<Advancement> create() {
        return List.of(
                FIRST,
                TENTH
        );
    }
}
