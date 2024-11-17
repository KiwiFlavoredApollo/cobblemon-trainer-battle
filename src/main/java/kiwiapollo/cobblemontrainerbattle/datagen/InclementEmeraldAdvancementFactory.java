package kiwiapollo.cobblemontrainerbattle.datagen;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.DefeatTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.advancement.KillTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.item.InclementEmeraldTokens;
import kiwiapollo.cobblemontrainerbattle.entity.InclementEmeraldPresets;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class InclementEmeraldAdvancementFactory implements AdvancementFactory {
    private static final Advancement DEFEAT_ROXANNE = Advancement.Builder.createUntelemetered()
            .parent(DataGenerator.AdvancementProvider.ROOT)
            .criterion("defeat_leader_roxanne", new DefeatTrainerCriterion.TrainerCountConditions(InclementEmeraldPresets.LEADER_ROXANNE.trainer()))
            .display(
                    InclementEmeraldTokens.LEADER_ROXANNE_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_roxanne.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_roxanne.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_roxanne"));

    private static final Advancement DEFEAT_BRAWLY = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_ROXANNE)
            .criterion("defeat_leader_brawly", new DefeatTrainerCriterion.TrainerCountConditions(InclementEmeraldPresets.LEADER_BRAWLY.trainer()))
            .display(
                    InclementEmeraldTokens.LEADER_BRAWLY_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_brawly.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_brawly.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_brawly"));

    private static final Advancement DEFEAT_WATTSON = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_BRAWLY)
            .criterion("defeat_leader_wattson", new DefeatTrainerCriterion.TrainerCountConditions(InclementEmeraldPresets.LEADER_WATTSON.trainer()))
            .display(
                    InclementEmeraldTokens.LEADER_WATTSON_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_wattson.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_wattson.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_wattson"));

    private static final Advancement DEFEAT_FLANNERY = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_WATTSON)
            .criterion("defeat_leader_flannery", new DefeatTrainerCriterion.TrainerCountConditions(InclementEmeraldPresets.LEADER_FLANNERY.trainer()))
            .display(
                    InclementEmeraldTokens.LEADER_FLANNERY_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_flannery.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_flannery.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_flannery"));

    private static final Advancement DEFEAT_NORMAN = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_FLANNERY)
            .criterion("defeat_leader_norman", new DefeatTrainerCriterion.TrainerCountConditions(InclementEmeraldPresets.LEADER_NORMAN.trainer()))
            .display(
                    InclementEmeraldTokens.LEADER_NORMAN_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_norman.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_norman.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_norman"));

    private static final Advancement DEFEAT_WINONA = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_NORMAN)
            .criterion("defeat_leader_winona", new DefeatTrainerCriterion.TrainerCountConditions(InclementEmeraldPresets.LEADER_WINONA.trainer()))
            .display(
                    InclementEmeraldTokens.LEADER_WINONA_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_winona.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_winona.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_winona"));

    private static final Advancement DEFEAT_TATE_AND_LIZA = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_WINONA)
            .criterion("defeat_leader_tate_and_liza", new DefeatTrainerCriterion.TrainerCountConditions(InclementEmeraldPresets.LEADER_TATE_AND_LIZA.trainer()))
            .display(
                    InclementEmeraldTokens.LEADER_TATE_AND_LIZA_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_tate_and_liza.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_tate_and_liza.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_tate_and_liza"));

    private static final Advancement DEFEAT_JUAN = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_TATE_AND_LIZA)
            .criterion("defeat_leader_juan", new DefeatTrainerCriterion.TrainerCountConditions(InclementEmeraldPresets.LEADER_JUAN.trainer()))
            .display(
                    InclementEmeraldTokens.LEADER_JUAN_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_juan.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_juan.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_juan"));

    private static final Advancement DEFEAT_SIDNEY = Advancement.Builder.createUntelemetered()
            .parent(DataGenerator.AdvancementProvider.ROOT)
            .criterion("defeat_elite_sidney", new DefeatTrainerCriterion.TrainerCountConditions(InclementEmeraldPresets.ELITE_SIDNEY.trainer()))
            .display(
                    InclementEmeraldTokens.ELITE_SIDNEY_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_sidney.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_sidney.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_sidney"));

    private static final Advancement DEFEAT_PHOEBE = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_SIDNEY)
            .criterion("defeat_elite_phoebe", new DefeatTrainerCriterion.TrainerCountConditions(InclementEmeraldPresets.ELITE_PHOEBE.trainer()))
            .display(
                    InclementEmeraldTokens.ELITE_PHOEBE_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_phoebe.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_phoebe.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_phoebe"));

    private static final Advancement DEFEAT_GLACIA = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_PHOEBE)
            .criterion("defeat_elite_glacia", new DefeatTrainerCriterion.TrainerCountConditions(InclementEmeraldPresets.ELITE_GLACIA.trainer()))
            .display(
                    InclementEmeraldTokens.ELITE_GLACIA_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_glacia.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_glacia.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_glacia"));

    private static final Advancement DEFEAT_DRAKE = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_GLACIA)
            .criterion("defeat_elite_drake", new DefeatTrainerCriterion.TrainerCountConditions(InclementEmeraldPresets.ELITE_DRAKE.trainer()))
            .display(
                    InclementEmeraldTokens.ELITE_DRAKE_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_drake.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_drake.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_drake"));

    private static final Advancement DEFEAT_WALLACE = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_DRAKE)
            .criterion("defeat_champion_wallace", new DefeatTrainerCriterion.TrainerCountConditions(InclementEmeraldPresets.CHAMPION_WALLACE.trainer()))
            .display(
                    InclementEmeraldTokens.CHAMPION_WALLACE_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_champion_wallace.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_champion_wallace.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.GOAL,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_champion_wallace"));

    private static final Advancement KILL_WALLACE = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_DRAKE)
            .criterion("kill_champion_wallace", new KillTrainerCriterion.TrainerCountConditions(InclementEmeraldPresets.CHAMPION_WALLACE.trainer()))
            .display(
                    Items.NETHERITE_SWORD,
                    Text.translatable("advancement.cobblemontrainerbattle.kill_champion_wallace.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.kill_champion_wallace.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "kill_champion_wallace"));

    @Override
    public List<Advancement> create() {
        return List.of(
                DEFEAT_ROXANNE,
                DEFEAT_BRAWLY,
                DEFEAT_WATTSON,
                DEFEAT_FLANNERY,
                DEFEAT_NORMAN,
                DEFEAT_WINONA,
                DEFEAT_TATE_AND_LIZA,
                DEFEAT_JUAN,
                DEFEAT_SIDNEY,
                DEFEAT_PHOEBE,
                DEFEAT_GLACIA,
                DEFEAT_DRAKE,
                DEFEAT_WALLACE,
                KILL_WALLACE
        );
    }
}
