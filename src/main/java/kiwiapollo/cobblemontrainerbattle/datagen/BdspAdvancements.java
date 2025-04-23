package kiwiapollo.cobblemontrainerbattle.datagen;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.DefeatTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.advancement.KillTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.item.BdspTokens;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public enum BdspAdvancements implements CustomAdvancements {
    DEFEAT_ROARK(Advancement.Builder.createUntelemetered()
            .parent(DataGenerator.AdvancementProvider.ROOT)
            .criterion("defeat_leader_roark", new DefeatTrainerCriterion.TrainerCountConditions("entity/leader_roark"))
            .display(
                    BdspTokens.LEADER_ROARK_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_roark.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_roark.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_roark"))),

    DEFEAT_GARDENIA(Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancements.DEFEAT_ROARK.getAdvancement())
            .criterion("defeat_leader_gardenia", new DefeatTrainerCriterion.TrainerCountConditions("entity/leader_gardenia"))
            .display(
                    BdspTokens.LEADER_GARDENIA_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_gardenia.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_gardenia.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_gardenia"))),

    DEFEAT_MAYLENE(Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancements.DEFEAT_GARDENIA.getAdvancement())
            .criterion("defeat_leader_maylene", new DefeatTrainerCriterion.TrainerCountConditions("entity/leader_maylene"))
            .display(
                    BdspTokens.LEADER_MAYLENE_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_maylene.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_maylene.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_maylene"))),

    DEFEAT_CRASHER_WAKE(Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancements.DEFEAT_MAYLENE.getAdvancement())
            .criterion("defeat_leader_crasher_wake", new DefeatTrainerCriterion.TrainerCountConditions("entity/leader_crasher_wake"))
            .display(
                    BdspTokens.LEADER_CRASHER_WAKE_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_crasher_wake.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_crasher_wake.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_crasher_wake"))),

    DEFEAT_FANTINA(Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancements.DEFEAT_CRASHER_WAKE.getAdvancement())
            .criterion("defeat_leader_fantina", new DefeatTrainerCriterion.TrainerCountConditions("entity/leader_fantina"))
            .display(
                    BdspTokens.LEADER_FANTINA_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_fantina.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_fantina.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_fantina"))),

    DEFEAT_BYRON(Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancements.DEFEAT_FANTINA.getAdvancement())
            .criterion("defeat_leader_byron", new DefeatTrainerCriterion.TrainerCountConditions("entity/leader_byron"))
            .display(
                    BdspTokens.LEADER_BYRON_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_byron.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_byron.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_byron"))),

    DEFEAT_CANDICE(Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancements.DEFEAT_BYRON.getAdvancement())
            .criterion("defeat_leader_candice", new DefeatTrainerCriterion.TrainerCountConditions("entity/leader_candice"))
            .display(
                    BdspTokens.LEADER_CANDICE_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_candice.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_candice.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_candice"))),

    DEFEAT_VOLKNER(Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancements.DEFEAT_CANDICE.getAdvancement())
            .criterion("defeat_leader_volkner", new DefeatTrainerCriterion.TrainerCountConditions("entity/leader_volkner"))
            .display(
                    BdspTokens.LEADER_VOLKNER_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_volkner.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_volkner.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_volkner"))),

    DEFEAT_AARON(Advancement.Builder.createUntelemetered()
            .parent(DataGenerator.AdvancementProvider.ROOT)
            .criterion("defeat_elite_aaron", new DefeatTrainerCriterion.TrainerCountConditions("entity/elite_aaron"))
            .display(
                    BdspTokens.ELITE_AARON_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_aaron.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_aaron.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_aaron"))),

    DEFEAT_BERTHA(Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancements.DEFEAT_AARON.getAdvancement())
            .criterion("defeat_elite_bertha", new DefeatTrainerCriterion.TrainerCountConditions("entity/elite_bertha"))
            .display(
                    BdspTokens.ELITE_BERTHA_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_bertha.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_bertha.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_bertha"))),

    DEFEAT_FLINT(Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancements.DEFEAT_BERTHA.getAdvancement())
            .criterion("defeat_elite_flint", new DefeatTrainerCriterion.TrainerCountConditions("entity/elite_flint"))
            .display(
                    BdspTokens.ELITE_FLINT_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_flint.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_flint.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_flint"))),

    DEFEAT_LUCIAN(Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancements.DEFEAT_FLINT.getAdvancement())
            .criterion("defeat_elite_lucian", new DefeatTrainerCriterion.TrainerCountConditions("entity/elite_lucian"))
            .display(
                    BdspTokens.ELITE_LUCIAN_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_lucian.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_lucian.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_lucian"))),

    DEFEAT_CYNTHIA(Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancements.DEFEAT_LUCIAN.getAdvancement())
            .criterion("defeat_champion_cynthia", new DefeatTrainerCriterion.TrainerCountConditions("entity/champion_cynthia"))
            .display(
                    BdspTokens.CHAMPION_CYNTHIA_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_champion_cynthia.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_champion_cynthia.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.GOAL,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_champion_cynthia"))),

    KILL_CYNTHIA(Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancements.DEFEAT_LUCIAN.getAdvancement())
            .criterion("kill_champion_cynthia", new KillTrainerCriterion.TrainerCountConditions("entity/champion_cynthia"))
            .display(
                    Items.NETHERITE_SWORD,
                    Text.translatable("advancement.cobblemontrainerbattle.kill_champion_cynthia.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.kill_champion_cynthia.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "kill_champion_cynthia")));

    private final Advancement advancement;

    BdspAdvancements(Advancement advancement) {
        this.advancement = advancement;
    }
    
    @Override
    public Advancement getAdvancement() {
        return advancement;
    }
}
