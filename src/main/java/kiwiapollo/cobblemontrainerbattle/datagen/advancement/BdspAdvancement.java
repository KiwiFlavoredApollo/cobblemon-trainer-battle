package kiwiapollo.cobblemontrainerbattle.datagen.advancement;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.DefeatTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.advancement.KillTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.item.token.BdspTokenItem;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public enum BdspAdvancement implements CustomAdvancement {
    DEFEAT_ROARK(Advancement.Builder.createUntelemetered()
            .parent(AdvancementProvider.ROOT)
            .criterion("defeat_leader_roark", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_roark")))
            .display(
                    BdspTokenItem.LEADER_ROARK_TOKEN.getItem(),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_roark.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_roark.description"),
                    AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_roark"))),

    DEFEAT_GARDENIA(Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancement.DEFEAT_ROARK.getAdvancement())
            .criterion("defeat_leader_gardenia", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_gardenia")))
            .display(
                    BdspTokenItem.LEADER_GARDENIA_TOKEN.getItem(),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_gardenia.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_gardenia.description"),
                    AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_gardenia"))),

    DEFEAT_MAYLENE(Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancement.DEFEAT_GARDENIA.getAdvancement())
            .criterion("defeat_leader_maylene", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_maylene")))
            .display(
                    BdspTokenItem.LEADER_MAYLENE_TOKEN.getItem(),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_maylene.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_maylene.description"),
                    AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_maylene"))),

    DEFEAT_CRASHER_WAKE(Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancement.DEFEAT_MAYLENE.getAdvancement())
            .criterion("defeat_leader_crasher_wake", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_crasher_wake")))
            .display(
                    BdspTokenItem.LEADER_CRASHER_WAKE_TOKEN.getItem(),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_crasher_wake.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_crasher_wake.description"),
                    AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_crasher_wake"))),

    DEFEAT_FANTINA(Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancement.DEFEAT_CRASHER_WAKE.getAdvancement())
            .criterion("defeat_leader_fantina", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_fantina")))
            .display(
                    BdspTokenItem.LEADER_FANTINA_TOKEN.getItem(),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_fantina.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_fantina.description"),
                    AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_fantina"))),

    DEFEAT_BYRON(Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancement.DEFEAT_FANTINA.getAdvancement())
            .criterion("defeat_leader_byron", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_byron")))
            .display(
                    BdspTokenItem.LEADER_BYRON_TOKEN.getItem(),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_byron.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_byron.description"),
                    AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_byron"))),

    DEFEAT_CANDICE(Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancement.DEFEAT_BYRON.getAdvancement())
            .criterion("defeat_leader_candice", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_candice")))
            .display(
                    BdspTokenItem.LEADER_CANDICE_TOKEN.getItem(),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_candice.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_candice.description"),
                    AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_candice"))),

    DEFEAT_VOLKNER(Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancement.DEFEAT_CANDICE.getAdvancement())
            .criterion("defeat_leader_volkner", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_volkner")))
            .display(
                    BdspTokenItem.LEADER_VOLKNER_TOKEN.getItem(),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_volkner.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_volkner.description"),
                    AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_volkner"))),

    DEFEAT_AARON(Advancement.Builder.createUntelemetered()
            .parent(AdvancementProvider.ROOT)
            .criterion("defeat_elite_aaron", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_aaron")))
            .display(
                    BdspTokenItem.ELITE_AARON_TOKEN.getItem(),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_aaron.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_aaron.description"),
                    AdvancementProvider.BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_aaron"))),

    DEFEAT_BERTHA(Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancement.DEFEAT_AARON.getAdvancement())
            .criterion("defeat_elite_bertha", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_bertha")))
            .display(
                    BdspTokenItem.ELITE_BERTHA_TOKEN.getItem(),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_bertha.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_bertha.description"),
                    AdvancementProvider.BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_bertha"))),

    DEFEAT_FLINT(Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancement.DEFEAT_BERTHA.getAdvancement())
            .criterion("defeat_elite_flint", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_flint")))
            .display(
                    BdspTokenItem.ELITE_FLINT_TOKEN.getItem(),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_flint.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_flint.description"),
                    AdvancementProvider.BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_flint"))),

    DEFEAT_LUCIAN(Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancement.DEFEAT_FLINT.getAdvancement())
            .criterion("defeat_elite_lucian", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_lucian")))
            .display(
                    BdspTokenItem.ELITE_LUCIAN_TOKEN.getItem(),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_lucian.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_lucian.description"),
                    AdvancementProvider.BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_lucian"))),

    DEFEAT_CYNTHIA(Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancement.DEFEAT_LUCIAN.getAdvancement())
            .criterion("defeat_champion_cynthia", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/champion_cynthia")))
            .display(
                    BdspTokenItem.CHAMPION_CYNTHIA_TOKEN.getItem(),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_champion_cynthia.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_champion_cynthia.description"),
                    AdvancementProvider.BACKGROUND,
                    AdvancementFrame.GOAL,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_champion_cynthia"))),

    KILL_CYNTHIA(Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancement.DEFEAT_LUCIAN.getAdvancement())
            .criterion("kill_champion_cynthia", new KillTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/champion_cynthia")))
            .display(
                    Items.NETHERITE_SWORD,
                    Text.translatable("advancements.cobblemontrainerbattle.kill_champion_cynthia.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.kill_champion_cynthia.description"),
                    AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "kill_champion_cynthia")));

    private final Advancement advancement;

    BdspAdvancement(Advancement advancement) {
        this.advancement = advancement;
    }
    
    @Override
    public Advancement getAdvancement() {
        return advancement;
    }
}
