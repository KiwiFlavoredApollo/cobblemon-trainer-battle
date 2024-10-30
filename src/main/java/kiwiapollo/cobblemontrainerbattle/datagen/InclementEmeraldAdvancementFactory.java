package kiwiapollo.cobblemontrainerbattle.datagen;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.DefeatTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.item.ItemRegistry;
import kiwiapollo.cobblemontrainerbattle.entities.InclementEmeraldTrainerEntityPresetRegistry;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class InclementEmeraldAdvancementFactory implements AdvancementFactory {
    private static final Advancement ROXANNE = Advancement.Builder.createUntelemetered()
            .parent(DataGenerator.AdvancementProvider.ROOT)
            .criterion("defeat_leader_roxanne", new DefeatTrainerCriterion.Conditions(InclementEmeraldTrainerEntityPresetRegistry.LEADER_ROXANNE.trainer()))
            .display(
                    ItemRegistry.LEADER_ROXANNE_TOKEN,
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_roxanne.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_roxanne.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_roxanne"));

    private static final Advancement BRAWLY = Advancement.Builder.createUntelemetered()
            .parent(ROXANNE)
            .criterion("defeat_leader_brawly", new DefeatTrainerCriterion.Conditions(InclementEmeraldTrainerEntityPresetRegistry.LEADER_BRAWLY.trainer()))
            .display(
                    ItemRegistry.LEADER_BRAWLY_TOKEN,
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_brawly.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_brawly.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_brawly"));

    private static final Advancement WATTSON = Advancement.Builder.createUntelemetered()
            .parent(BRAWLY)
            .criterion("defeat_leader_wattson", new DefeatTrainerCriterion.Conditions(InclementEmeraldTrainerEntityPresetRegistry.LEADER_WATTSON.trainer()))
            .display(
                    ItemRegistry.LEADER_WATTSON_TOKEN,
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_wattson.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_wattson.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_wattson"));

    private static final Advancement FLANNERY = Advancement.Builder.createUntelemetered()
            .parent(WATTSON)
            .criterion("defeat_leader_flannery", new DefeatTrainerCriterion.Conditions(InclementEmeraldTrainerEntityPresetRegistry.LEADER_FLANNERY.trainer()))
            .display(
                    ItemRegistry.LEADER_FLANNERY_TOKEN,
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_flannery.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_flannery.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_flannery"));

    private static final Advancement NORMAN = Advancement.Builder.createUntelemetered()
            .parent(FLANNERY)
            .criterion("defeat_leader_norman", new DefeatTrainerCriterion.Conditions(InclementEmeraldTrainerEntityPresetRegistry.LEADER_NORMAN.trainer()))
            .display(
                    ItemRegistry.LEADER_NORMAN_TOKEN,
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_norman.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_norman.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_norman"));

    private static final Advancement WINONA = Advancement.Builder.createUntelemetered()
            .parent(NORMAN)
            .criterion("defeat_leader_winona", new DefeatTrainerCriterion.Conditions(InclementEmeraldTrainerEntityPresetRegistry.LEADER_WINONA.trainer()))
            .display(
                    ItemRegistry.LEADER_WINONA_TOKEN,
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_winona.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_winona.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_winona"));

    private static final Advancement TATE_AND_LIZA = Advancement.Builder.createUntelemetered()
            .parent(WINONA)
            .criterion("defeat_leader_tate_and_liza", new DefeatTrainerCriterion.Conditions(InclementEmeraldTrainerEntityPresetRegistry.LEADER_TATE_AND_LIZA.trainer()))
            .display(
                    ItemRegistry.LEADER_TATE_AND_LIZA_TOKEN,
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_tate_and_liza.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_tate_and_liza.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_tate_and_liza"));

    private static final Advancement JUAN = Advancement.Builder.createUntelemetered()
            .parent(TATE_AND_LIZA)
            .criterion("defeat_leader_juan", new DefeatTrainerCriterion.Conditions(InclementEmeraldTrainerEntityPresetRegistry.LEADER_JUAN.trainer()))
            .display(
                    ItemRegistry.LEADER_JUAN_TOKEN,
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_juan.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_juan.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_juan"));

    private static final Advancement SIDNEY = Advancement.Builder.createUntelemetered()
            .parent(DataGenerator.AdvancementProvider.ROOT)
            .criterion("defeat_elite_sidney", new DefeatTrainerCriterion.Conditions(InclementEmeraldTrainerEntityPresetRegistry.ELITE_SIDNEY.trainer()))
            .display(
                    ItemRegistry.ELITE_SIDNEY_TOKEN,
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_sidney.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_sidney.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_sidney"));

    private static final Advancement PHOEBE = Advancement.Builder.createUntelemetered()
            .parent(SIDNEY)
            .criterion("defeat_elite_phoebe", new DefeatTrainerCriterion.Conditions(InclementEmeraldTrainerEntityPresetRegistry.ELITE_PHOEBE.trainer()))
            .display(
                    ItemRegistry.ELITE_PHOEBE_TOKEN,
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_phoebe.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_phoebe.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_phoebe"));

    private static final Advancement GLACIA = Advancement.Builder.createUntelemetered()
            .parent(PHOEBE)
            .criterion("defeat_elite_glacia", new DefeatTrainerCriterion.Conditions(InclementEmeraldTrainerEntityPresetRegistry.ELITE_GLACIA.trainer()))
            .display(
                    ItemRegistry.ELITE_GLACIA_TOKEN,
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_glacia.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_glacia.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_glacia"));

    private static final Advancement DRAKE = Advancement.Builder.createUntelemetered()
            .parent(GLACIA)
            .criterion("defeat_elite_drake", new DefeatTrainerCriterion.Conditions(InclementEmeraldTrainerEntityPresetRegistry.ELITE_DRAKE.trainer()))
            .display(
                    ItemRegistry.ELITE_DRAKE_TOKEN,
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_drake.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_drake.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_drake"));

    private static final Advancement WALLACE = Advancement.Builder.createUntelemetered()
            .parent(DRAKE)
            .criterion("defeat_champion_wallace", new DefeatTrainerCriterion.Conditions(InclementEmeraldTrainerEntityPresetRegistry.CHAMPION_WALLACE.trainer()))
            .display(
                    ItemRegistry.CHAMPION_WALLACE_TOKEN,
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_champion_wallace.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_champion_wallace.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_champion_wallace"));

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
                JUAN,
                SIDNEY,
                PHOEBE,
                GLACIA,
                DRAKE,
                WALLACE
        );
    }
}
