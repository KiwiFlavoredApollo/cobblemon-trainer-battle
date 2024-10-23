package kiwiapollo.cobblemontrainerbattle.datagen;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.DefeatTrainerCriterion;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class DefeatTrainerAdvancementFactory implements AdvancementFactory {
    private static final Advancement FIRST = Advancement.Builder.createUntelemetered()
            .parent(DataGenerator.AdvancementProvider.ROOT)
            .criterion("defeat_first_trainer", new DefeatTrainerCriterion.Conditions())
            .display(
                    Items.BELL,
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_trainer.first.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_trainer.first.description"),
                    new Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.NAMESPACE, "defeat_first_trainer"));

    public DefeatTrainerAdvancementFactory() {

    }

    @Override
    public List<Advancement> create() {
        return List.of(
                FIRST
        );
    }
}
