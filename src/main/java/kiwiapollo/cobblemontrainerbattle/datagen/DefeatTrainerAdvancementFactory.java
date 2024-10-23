package kiwiapollo.cobblemontrainerbattle.datagen;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.DefeatTrainerCriterion;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class DefeatTrainerAdvancementFactory implements AdvancementFactory {
    private static final Advancement FIRST = Advancement.Builder.createUntelemetered()
            .parent(DataGenerator.AdvancementProvider.ROOT)
            .criterion("defeat_first_trainer", new DefeatTrainerCriterion.Conditions(1))
            .display(
                    Registries.ITEM.get(Identifier.of("cobblemon", "poke_ball")),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_trainer.first.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_trainer.first.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.NAMESPACE, "defeat_first_trainer"));

    private static final Advancement TENTH = Advancement.Builder.createUntelemetered()
            .parent(FIRST)
            .criterion("defeat_tenth_trainer", new DefeatTrainerCriterion.Conditions(10))
            .display(
                    Registries.ITEM.get(Identifier.of("cobblemon", "great_ball")),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_trainer.tenth.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_trainer.tenth.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.NAMESPACE, "defeat_tenth_trainer"));

    public DefeatTrainerAdvancementFactory() {

    }

    @Override
    public List<Advancement> create() {
        return List.of(
                FIRST,
                TENTH
        );
    }
}
