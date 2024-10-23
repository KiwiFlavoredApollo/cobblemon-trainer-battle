package kiwiapollo.cobblemontrainerbattle.datagen;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.DefeatTrainerCriterion;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class InclementEmeraldAdvancementFactory implements AdvancementFactory {
    private static final Advancement ROXANNE = Advancement.Builder.createUntelemetered()
            .parent(DataGenerator.AdvancementProvider.ROOT)
            .criterion("defeat_roxanne", new DefeatTrainerCriterion.Conditions(1)) // TODO
            .display(
                    Registries.ITEM.get(Identifier.of("cobblemon", "poke_ball")),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_roxanne.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_roxanne.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.NAMESPACE, "defeat_roxanne"));

    private static final Advancement BRAWLY = Advancement.Builder.createUntelemetered()
            .parent(ROXANNE)
            .criterion("defeat_brawly", new DefeatTrainerCriterion.Conditions(1)) // TODO
            .display(
                    Registries.ITEM.get(Identifier.of("cobblemon", "poke_ball")),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_brawly.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_brawly.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.NAMESPACE, "defeat_brawly"));

    @Override
    public List<Advancement> create() {
        return List.of(
                ROXANNE,
                BRAWLY
        );
    }
}
