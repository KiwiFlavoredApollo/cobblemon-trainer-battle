package kiwiapollo.cobblemontrainerbattle.datagen;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.DefeatTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.item.ItemRegistry;
import kiwiapollo.cobblemontrainerbattle.trainerpreset.InclementEmeraldTrainerEntityPresetRegistry;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class InclementEmeraldAdvancementFactory implements AdvancementFactory {
    private static final Advancement ROXANNE = Advancement.Builder.createUntelemetered()
            .parent(DataGenerator.AdvancementProvider.ROOT)
            .criterion("defeat_roxanne", new DefeatTrainerCriterion.Conditions(InclementEmeraldTrainerEntityPresetRegistry.LEADER_ROXANNE.trainer()))
            .display(
                    ItemRegistry.INCLEMENTEMERALD_LEADER_ROXANNE_TOKEN,
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
            .criterion("defeat_brawly", new DefeatTrainerCriterion.Conditions(InclementEmeraldTrainerEntityPresetRegistry.LEADER_BRAWLY.trainer()))
            .display(
                    ItemRegistry.INCLEMENTEMERALD_LEADER_BRAWLY_TOKEN,
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_brawly.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_brawly.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.NAMESPACE, "defeat_brawly"));

    private static final Advancement WATTSON = Advancement.Builder.createUntelemetered()
            .parent(BRAWLY)
            .criterion("defeat_wattson", new DefeatTrainerCriterion.Conditions(InclementEmeraldTrainerEntityPresetRegistry.LEADER_WATTSON.trainer()))
            .display(
                    ItemRegistry.INCLEMENTEMERALD_LEADER_WATTSON_TOKEN,
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_wattson.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_wattson.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.NAMESPACE, "defeat_wattson"));

    private static final Advancement FLANNERY = Advancement.Builder.createUntelemetered()
            .parent(WATTSON)
            .criterion("defeat_flannery", new DefeatTrainerCriterion.Conditions(InclementEmeraldTrainerEntityPresetRegistry.LEADER_FLANNERY.trainer()))
            .display(
                    ItemRegistry.INCLEMENTEMERALD_LEADER_FLANNERY_TOKEN,
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_flannery.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_flannery.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.NAMESPACE, "defeat_flannery"));

    private static final Advancement NORMAN = Advancement.Builder.createUntelemetered()
            .parent(FLANNERY)
            .criterion("defeat_norman", new DefeatTrainerCriterion.Conditions(InclementEmeraldTrainerEntityPresetRegistry.LEADER_NORMAN.trainer()))
            .display(
                    ItemRegistry.INCLEMENTEMERALD_LEADER_NORMAN_TOKEN,
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_norman.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_norman.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.NAMESPACE, "defeat_norman"));

    private static final Advancement WINONA = Advancement.Builder.createUntelemetered()
            .parent(NORMAN)
            .criterion("defeat_winona", new DefeatTrainerCriterion.Conditions(InclementEmeraldTrainerEntityPresetRegistry.LEADER_WINONA.trainer()))
            .display(
                    ItemRegistry.INCLEMENTEMERALD_LEADER_WINONA_TOKEN,
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_winona.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_winona.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.NAMESPACE, "defeat_winona"));

    private static final Advancement TATE_AND_LIZA = Advancement.Builder.createUntelemetered()
            .parent(WINONA)
            .criterion("defeat_tate_and_liza", new DefeatTrainerCriterion.Conditions(InclementEmeraldTrainerEntityPresetRegistry.LEADER_TATE_AND_LIZA.trainer()))
            .display(
                    ItemRegistry.INCLEMENTEMERALD_LEADER_TATE_AND_LIZA_TOKEN,
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_tate_and_liza.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_tate_and_liza.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.NAMESPACE, "defeat_tate_and_liza"));

    private static final Advancement JUAN = Advancement.Builder.createUntelemetered()
            .parent(TATE_AND_LIZA)
            .criterion("defeat_juan", new DefeatTrainerCriterion.Conditions(InclementEmeraldTrainerEntityPresetRegistry.LEADER_JUAN.trainer()))
            .display(
                    ItemRegistry.INCLEMENTEMERALD_LEADER_JUAN_TOKEN,
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_juan.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_juan.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.NAMESPACE, "defeat_juan"));

    @Override
    public List<Advancement> create() {
        return List.of(
                ROXANNE,
                BRAWLY,
                WATTSON,
                FLANNERY,
                NORMAN,
                WINONA,
                TATE_AND_LIZA,
                JUAN
        );
    }
}
