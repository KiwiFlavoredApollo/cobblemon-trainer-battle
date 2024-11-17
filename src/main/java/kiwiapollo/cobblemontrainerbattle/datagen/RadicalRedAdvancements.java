package kiwiapollo.cobblemontrainerbattle.datagen;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.DefeatTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.advancement.KillTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.entity.RadicalRedPresets;
import kiwiapollo.cobblemontrainerbattle.item.RadicalRedTokens;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public enum RadicalRedAdvancements implements CustomAdvancements {
    DEFEAT_BROCK(Advancement.Builder.createUntelemetered()
            .parent(DataGenerator.AdvancementProvider.ROOT)
            .criterion("defeat_leader_brock", new DefeatTrainerCriterion.TrainerCountConditions(RadicalRedPresets.LEADER_BROCK.trainer()))
            .display(
                    RadicalRedTokens.LEADER_BROCK_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_brock.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_brock.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_brock"))),

    DEFEAT_MISTY(Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancements.DEFEAT_BROCK.getAdvancement())
            .criterion("defeat_leader_misty", new DefeatTrainerCriterion.TrainerCountConditions(RadicalRedPresets.LEADER_MISTY.trainer()))
            .display(
                    RadicalRedTokens.LEADER_MISTY_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_misty.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_misty.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_misty"))),

    DEFEAT_LT_SURGE(Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancements.DEFEAT_MISTY.getAdvancement())
            .criterion("defeat_leader_lt_surge", new DefeatTrainerCriterion.TrainerCountConditions(RadicalRedPresets.LEADER_LT_SURGE.trainer()))
            .display(
                    RadicalRedTokens.LEADER_LT_SURGE_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_lt_surge.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_lt_surge.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_lt_surge"))),

    DEFEAT_ERIKA(Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancements.DEFEAT_LT_SURGE.getAdvancement())
            .criterion("defeat_leader_erika", new DefeatTrainerCriterion.TrainerCountConditions(RadicalRedPresets.LEADER_ERIKA.trainer()))
            .display(
                    RadicalRedTokens.LEADER_ERIKA_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_erika.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_erika.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_erika"))),

    DEFEAT_KOGA(Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancements.DEFEAT_ERIKA.getAdvancement())
            .criterion("defeat_leader_koga", new DefeatTrainerCriterion.TrainerCountConditions(RadicalRedPresets.LEADER_KOGA.trainer()))
            .display(
                    RadicalRedTokens.LEADER_KOGA_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_koga.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_koga.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_koga"))),

    DEFEAT_SABRINA(Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancements.DEFEAT_KOGA.getAdvancement())
            .criterion("defeat_leader_sabrina", new DefeatTrainerCriterion.TrainerCountConditions(RadicalRedPresets.LEADER_SABRINA.trainer()))
            .display(
                    RadicalRedTokens.LEADER_SABRINA_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_sabrina.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_sabrina.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_sabrina"))),

    DEFEAT_BLAINE(Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancements.DEFEAT_SABRINA.getAdvancement())
            .criterion("defeat_leader_blaine", new DefeatTrainerCriterion.TrainerCountConditions(RadicalRedPresets.LEADER_BLAINE.trainer()))
            .display(
                    RadicalRedTokens.LEADER_BLAINE_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_blaine.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_blaine.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_blaine"))),

    DEFEAT_GIOVANNI(Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancements.DEFEAT_BLAINE.getAdvancement())
            .criterion("defeat_leader_giovanni", new DefeatTrainerCriterion.TrainerCountConditions(RadicalRedPresets.LEADER_GIOVANNI.trainer()))
            .display(
                    RadicalRedTokens.LEADER_GIOVANNI_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_giovanni.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_giovanni.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_giovanni"))),

    DEFEAT_FALKNER(Advancement.Builder.createUntelemetered()
            .parent(DataGenerator.AdvancementProvider.ROOT)
            .criterion("defeat_leader_falkner", new DefeatTrainerCriterion.TrainerCountConditions(RadicalRedPresets.LEADER_FALKNER.trainer()))
            .display(
                    RadicalRedTokens.LEADER_FALKNER_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_falkner.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_falkner.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_falkner"))),

    DEFEAT_BUGSY(Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancements.DEFEAT_FALKNER.getAdvancement())
            .criterion("defeat_leader_bugsy", new DefeatTrainerCriterion.TrainerCountConditions(RadicalRedPresets.LEADER_BUGSY.trainer()))
            .display(
                    RadicalRedTokens.LEADER_BUGSY_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_bugsy.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_bugsy.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_bugsy"))),

    DEFEAT_WHITNEY(Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancements.DEFEAT_BUGSY.getAdvancement())
            .criterion("defeat_leader_whitney", new DefeatTrainerCriterion.TrainerCountConditions(RadicalRedPresets.LEADER_WHITNEY.trainer()))
            .display(
                    RadicalRedTokens.LEADER_WHITNEY_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_whitney.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_whitney.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_whitney"))),

    DEFEAT_MORTY(Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancements.DEFEAT_WHITNEY.getAdvancement())
            .criterion("defeat_leader_morty", new DefeatTrainerCriterion.TrainerCountConditions(RadicalRedPresets.LEADER_MORTY.trainer()))
            .display(
                    RadicalRedTokens.LEADER_MORTY_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_morty.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_morty.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_morty"))),

    DEFEAT_CHUCK(Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancements.DEFEAT_MORTY.getAdvancement())
            .criterion("defeat_leader_chuck", new DefeatTrainerCriterion.TrainerCountConditions(RadicalRedPresets.LEADER_CHUCK.trainer()))
            .display(
                    RadicalRedTokens.LEADER_CHUCK_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_chuck.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_chuck.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_chuck"))),

    DEFEAT_JASMINE(Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancements.DEFEAT_CHUCK.getAdvancement())
            .criterion("defeat_leader_jasmine", new DefeatTrainerCriterion.TrainerCountConditions(RadicalRedPresets.LEADER_JASMINE.trainer()))
            .display(
                    RadicalRedTokens.LEADER_JASMINE_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_jasmine.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_jasmine.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_jasmine"))),

    DEFEAT_PRYCE(Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancements.DEFEAT_JASMINE.getAdvancement())
            .criterion("defeat_leader_pryce", new DefeatTrainerCriterion.TrainerCountConditions(RadicalRedPresets.LEADER_PRYCE.trainer()))
            .display(
                    RadicalRedTokens.LEADER_PRYCE_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_pryce.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_pryce.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_pryce"))),

    DEFEAT_CLAIR(Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancements.DEFEAT_PRYCE.getAdvancement())
            .criterion("defeat_leader_clair", new DefeatTrainerCriterion.TrainerCountConditions(RadicalRedPresets.LEADER_CLAIR.trainer()))
            .display(
                    RadicalRedTokens.LEADER_CLAIR_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_clair.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_clair.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_clair"))),

    DEFEAT_LORELEI(Advancement.Builder.createUntelemetered()
            .parent(DataGenerator.AdvancementProvider.ROOT)
            .criterion("defeat_elite_lorelei", new DefeatTrainerCriterion.TrainerCountConditions(RadicalRedPresets.ELITE_LORELEI.trainer()))
            .display(
                    RadicalRedTokens.ELITE_LORELEI_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_lorelei.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_lorelei.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_lorelei"))),

    DEFEAT_BRUNO(Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancements.DEFEAT_LORELEI.getAdvancement())
            .criterion("defeat_elite_bruno", new DefeatTrainerCriterion.TrainerCountConditions(RadicalRedPresets.ELITE_BRUNO.trainer()))
            .display(
                    RadicalRedTokens.ELITE_BRUNO_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_bruno.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_bruno.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_bruno"))),

    DEFEAT_AGATHA(Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancements.DEFEAT_BRUNO.getAdvancement())
            .criterion("defeat_elite_agatha", new DefeatTrainerCriterion.TrainerCountConditions(RadicalRedPresets.ELITE_AGATHA.trainer()))
            .display(
                    RadicalRedTokens.ELITE_AGATHA_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_agatha.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_agatha.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_agatha"))),

    DEFEAT_LANCE(Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancements.DEFEAT_AGATHA.getAdvancement())
            .criterion("defeat_elite_lance", new DefeatTrainerCriterion.TrainerCountConditions(RadicalRedPresets.ELITE_LANCE.trainer()))
            .display(
                    RadicalRedTokens.ELITE_LANCE_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_lance.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_lance.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_lance"))),

    DEFEAT_TERRY(Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancements.DEFEAT_LANCE.getAdvancement())
            .criterion("defeat_champion_terry", new DefeatTrainerCriterion.TrainerCountConditions(RadicalRedPresets.CHAMPION_TERRY.trainer()))
            .display(
                    RadicalRedTokens.CHAMPION_TERRY_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_champion_terry.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_champion_terry.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.GOAL,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_champion_terry"))),

    KILL_TERRY(Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancements.DEFEAT_LANCE.getAdvancement())
            .criterion("kill_champion_terry", new KillTrainerCriterion.TrainerCountConditions(RadicalRedPresets.CHAMPION_TERRY.trainer()))
            .display(
                    Items.NETHERITE_SWORD,
                    Text.translatable("advancement.cobblemontrainerbattle.kill_champion_terry.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.kill_champion_terry.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "kill_champion_terry")));

    private final Advancement advancement;

    RadicalRedAdvancements(Advancement advancement) {
        this.advancement = advancement;
    }
    
    @Override
    public Advancement getAdvancement() {
        return advancement;
    }
}
