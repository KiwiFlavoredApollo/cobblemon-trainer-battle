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

public enum InclementEmeraldAdvancements implements CustomAdvancements {
    DEFEAT_ROXANNE(Advancement.Builder.createUntelemetered()
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_roxanne"))),

    DEFEAT_BRAWLY(Advancement.Builder.createUntelemetered()
            .parent(InclementEmeraldAdvancements.DEFEAT_ROXANNE.getAdvancement())
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_brawly"))),

    DEFEAT_WATTSON(Advancement.Builder.createUntelemetered()
            .parent(InclementEmeraldAdvancements.DEFEAT_BRAWLY.getAdvancement())
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_wattson"))),

    DEFEAT_FLANNERY(Advancement.Builder.createUntelemetered()
            .parent(InclementEmeraldAdvancements.DEFEAT_WATTSON.getAdvancement())
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_flannery"))),

    DEFEAT_NORMAN(Advancement.Builder.createUntelemetered()
            .parent(InclementEmeraldAdvancements.DEFEAT_FLANNERY.getAdvancement())
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_norman"))),

    DEFEAT_WINONA(Advancement.Builder.createUntelemetered()
            .parent(InclementEmeraldAdvancements.DEFEAT_NORMAN.getAdvancement())
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_winona"))),

    DEFEAT_TATE_AND_LIZA(Advancement.Builder.createUntelemetered()
            .parent(InclementEmeraldAdvancements.DEFEAT_WINONA.getAdvancement())
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_tate_and_liza"))),

    DEFEAT_JUAN(Advancement.Builder.createUntelemetered()
            .parent(InclementEmeraldAdvancements.DEFEAT_TATE_AND_LIZA.getAdvancement())
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_juan"))),

    DEFEAT_SIDNEY(Advancement.Builder.createUntelemetered()
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_sidney"))),

    DEFEAT_PHOEBE(Advancement.Builder.createUntelemetered()
            .parent(InclementEmeraldAdvancements.DEFEAT_SIDNEY.getAdvancement())
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_phoebe"))),

    DEFEAT_GLACIA(Advancement.Builder.createUntelemetered()
            .parent(InclementEmeraldAdvancements.DEFEAT_PHOEBE.getAdvancement())
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_glacia"))),

    DEFEAT_DRAKE(Advancement.Builder.createUntelemetered()
            .parent(InclementEmeraldAdvancements.DEFEAT_GLACIA.getAdvancement())
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_drake"))),

    DEFEAT_WALLACE(Advancement.Builder.createUntelemetered()
            .parent(InclementEmeraldAdvancements.DEFEAT_DRAKE.getAdvancement())
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_champion_wallace"))),

    KILL_WALLACE(Advancement.Builder.createUntelemetered()
            .parent(InclementEmeraldAdvancements.DEFEAT_DRAKE.getAdvancement())
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "kill_champion_wallace")));

    private final Advancement advancement;
    
    InclementEmeraldAdvancements(Advancement advancement) {
        this.advancement = advancement;
    }
    
    @Override
    public Advancement getAdvancement() {
        return advancement;
    }
}
