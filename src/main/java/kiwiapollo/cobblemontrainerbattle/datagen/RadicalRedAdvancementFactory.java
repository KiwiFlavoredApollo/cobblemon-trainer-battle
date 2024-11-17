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

public class RadicalRedAdvancementFactory implements AdvancementFactory {
    private static final Advancement DEFEAT_BROCK = Advancement.Builder.createUntelemetered()
            .parent(DataGenerator.AdvancementProvider.ROOT)
            .criterion("defeat_leader_brock", new DefeatTrainerCriterion.Conditions(RadicalRedPresets.LEADER_BROCK.trainer()))
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_brock"));

    private static final Advancement DEFEAT_MISTY = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_BROCK)
            .criterion("defeat_leader_misty", new DefeatTrainerCriterion.Conditions(RadicalRedPresets.LEADER_MISTY.trainer()))
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_misty"));

    private static final Advancement DEFEAT_LT_SURGE = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_MISTY)
            .criterion("defeat_leader_lt_surge", new DefeatTrainerCriterion.Conditions(RadicalRedPresets.LEADER_LT_SURGE.trainer()))
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_lt_surge"));

    private static final Advancement DEFEAT_ERIKA = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_LT_SURGE)
            .criterion("defeat_leader_erika", new DefeatTrainerCriterion.Conditions(RadicalRedPresets.LEADER_ERIKA.trainer()))
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_erika"));

    private static final Advancement DEFEAT_KOGA = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_ERIKA)
            .criterion("defeat_leader_koga", new DefeatTrainerCriterion.Conditions(RadicalRedPresets.LEADER_KOGA.trainer()))
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_koga"));

    private static final Advancement DEFEAT_SABRINA = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_KOGA)
            .criterion("defeat_leader_sabrina", new DefeatTrainerCriterion.Conditions(RadicalRedPresets.LEADER_SABRINA.trainer()))
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_sabrina"));

    private static final Advancement DEFEAT_BLAINE = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_SABRINA)
            .criterion("defeat_leader_blaine", new DefeatTrainerCriterion.Conditions(RadicalRedPresets.LEADER_BLAINE.trainer()))
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_blaine"));

    private static final Advancement DEFEAT_GIOVANNI = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_BLAINE)
            .criterion("defeat_leader_giovanni", new DefeatTrainerCriterion.Conditions(RadicalRedPresets.LEADER_GIOVANNI.trainer()))
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_giovanni"));

    private static final Advancement DEFEAT_FALKNER = Advancement.Builder.createUntelemetered()
            .parent(DataGenerator.AdvancementProvider.ROOT)
            .criterion("defeat_leader_falkner", new DefeatTrainerCriterion.Conditions(RadicalRedPresets.LEADER_FALKNER.trainer()))
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_falkner"));

    private static final Advancement DEFEAT_BUGSY = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_FALKNER)
            .criterion("defeat_leader_bugsy", new DefeatTrainerCriterion.Conditions(RadicalRedPresets.LEADER_BUGSY.trainer()))
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_bugsy"));

    private static final Advancement DEFEAT_WHITNEY = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_BUGSY)
            .criterion("defeat_leader_whitney", new DefeatTrainerCriterion.Conditions(RadicalRedPresets.LEADER_WHITNEY.trainer()))
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_whitney"));

    private static final Advancement DEFEAT_MORTY = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_WHITNEY)
            .criterion("defeat_leader_morty", new DefeatTrainerCriterion.Conditions(RadicalRedPresets.LEADER_MORTY.trainer()))
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_morty"));

    private static final Advancement DEFEAT_CHUCK = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_MORTY)
            .criterion("defeat_leader_chuck", new DefeatTrainerCriterion.Conditions(RadicalRedPresets.LEADER_CHUCK.trainer()))
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_chuck"));

    private static final Advancement DEFEAT_JASMINE = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_CHUCK)
            .criterion("defeat_leader_jasmine", new DefeatTrainerCriterion.Conditions(RadicalRedPresets.LEADER_JASMINE.trainer()))
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_jasmine"));

    private static final Advancement DEFEAT_PRYCE = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_JASMINE)
            .criterion("defeat_leader_pryce", new DefeatTrainerCriterion.Conditions(RadicalRedPresets.LEADER_PRYCE.trainer()))
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_pryce"));

    private static final Advancement DEFEAT_CLAIR = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_PRYCE)
            .criterion("defeat_leader_clair", new DefeatTrainerCriterion.Conditions(RadicalRedPresets.LEADER_CLAIR.trainer()))
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_clair"));

    private static final Advancement DEFEAT_LORELEI = Advancement.Builder.createUntelemetered()
            .parent(DataGenerator.AdvancementProvider.ROOT)
            .criterion("defeat_elite_lorelei", new DefeatTrainerCriterion.Conditions(RadicalRedPresets.ELITE_LORELEI.trainer()))
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_lorelei"));

    private static final Advancement DEFEAT_BRUNO = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_LORELEI)
            .criterion("defeat_elite_bruno", new DefeatTrainerCriterion.Conditions(RadicalRedPresets.ELITE_BRUNO.trainer()))
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_bruno"));

    private static final Advancement DEFEAT_AGATHA = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_BRUNO)
            .criterion("defeat_elite_agatha", new DefeatTrainerCriterion.Conditions(RadicalRedPresets.ELITE_AGATHA.trainer()))
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_agatha"));

    private static final Advancement DEFEAT_LANCE = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_AGATHA)
            .criterion("defeat_elite_lance", new DefeatTrainerCriterion.Conditions(RadicalRedPresets.ELITE_LANCE.trainer()))
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_lance"));

    private static final Advancement DEFEAT_TERRY = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_LANCE)
            .criterion("defeat_champion_terry", new DefeatTrainerCriterion.Conditions(RadicalRedPresets.CHAMPION_TERRY.trainer()))
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_champion_terry"));

    private static final Advancement KILL_TERRY = Advancement.Builder.createUntelemetered()
            .parent(DEFEAT_LANCE)
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
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "kill_champion_terry"));

    @Override
    public List<Advancement> create() {
        return List.of(
                DEFEAT_BROCK,
                DEFEAT_MISTY,
                DEFEAT_LT_SURGE,
                DEFEAT_ERIKA,
                DEFEAT_KOGA,
                DEFEAT_SABRINA,
                DEFEAT_BLAINE,
                DEFEAT_GIOVANNI,

                DEFEAT_FALKNER,
                DEFEAT_BUGSY,
                DEFEAT_WHITNEY,
                DEFEAT_MORTY,
                DEFEAT_CHUCK,
                DEFEAT_JASMINE,
                DEFEAT_PRYCE,
                DEFEAT_CLAIR,

                DEFEAT_LORELEI,
                DEFEAT_BRUNO,
                DEFEAT_AGATHA,
                DEFEAT_LANCE,
                DEFEAT_TERRY,
                KILL_TERRY
        );
    }
}
