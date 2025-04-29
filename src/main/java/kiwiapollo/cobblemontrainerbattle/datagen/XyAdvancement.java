package kiwiapollo.cobblemontrainerbattle.datagen;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.DefeatTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.advancement.KillTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.item.XyTokenItem;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public enum XyAdvancement implements CustomAdvancement {
    DEFEAT_VIOLA(Advancement.Builder.createUntelemetered()
            .parent(DataGenerator.AdvancementProvider.ROOT)
            .criterion("defeat_leader_viola", new DefeatTrainerCriterion.TrainerCountConditions("entity/leader_viola"))
            .display(
                    XyTokenItem.LEADER_VIOLA_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_viola.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_viola.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_viola"))),

    DEFEAT_GRANT(Advancement.Builder.createUntelemetered()
            .parent(XyAdvancement.DEFEAT_VIOLA.getAdvancement())
            .criterion("defeat_leader_grant", new DefeatTrainerCriterion.TrainerCountConditions("entity/leader_grant"))
            .display(
                    XyTokenItem.LEADER_GRANT_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_grant.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_grant.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_grant"))),

    DEFEAT_KORRINA(Advancement.Builder.createUntelemetered()
            .parent(XyAdvancement.DEFEAT_GRANT.getAdvancement())
            .criterion("defeat_leader_korrina", new DefeatTrainerCriterion.TrainerCountConditions("entity/leader_korrina"))
            .display(
                    XyTokenItem.LEADER_KORRINA_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_korrina.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_korrina.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_korrina"))),

    DEFEAT_RAMOS(Advancement.Builder.createUntelemetered()
            .parent(XyAdvancement.DEFEAT_KORRINA.getAdvancement())
            .criterion("defeat_leader_ramos", new DefeatTrainerCriterion.TrainerCountConditions("entity/leader_ramos"))
            .display(
                    XyTokenItem.LEADER_RAMOS_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_ramos.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_ramos.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_ramos"))),

    DEFEAT_CLEMONT(Advancement.Builder.createUntelemetered()
            .parent(XyAdvancement.DEFEAT_RAMOS.getAdvancement())
            .criterion("defeat_leader_clemont", new DefeatTrainerCriterion.TrainerCountConditions("entity/leader_clemont"))
            .display(
                    XyTokenItem.LEADER_CLEMONT_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_clemont.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_clemont.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_clemont"))),

    DEFEAT_VALERIE(Advancement.Builder.createUntelemetered()
            .parent(XyAdvancement.DEFEAT_CLEMONT.getAdvancement())
            .criterion("defeat_leader_valerie", new DefeatTrainerCriterion.TrainerCountConditions("entity/leader_valerie"))
            .display(
                    XyTokenItem.LEADER_VALERIE_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_valerie.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_valerie.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_valerie"))),

    DEFEAT_OLYMPIA(Advancement.Builder.createUntelemetered()
            .parent(XyAdvancement.DEFEAT_VALERIE.getAdvancement())
            .criterion("defeat_leader_olympia", new DefeatTrainerCriterion.TrainerCountConditions("entity/leader_olympia"))
            .display(
                    XyTokenItem.LEADER_OLYMPIA_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_olympia.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_olympia.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_olympia"))),

    DEFEAT_WULFRIC(Advancement.Builder.createUntelemetered()
            .parent(XyAdvancement.DEFEAT_OLYMPIA.getAdvancement())
            .criterion("defeat_leader_wulfric", new DefeatTrainerCriterion.TrainerCountConditions("entity/leader_wulfric"))
            .display(
                    XyTokenItem.LEADER_WULFRIC_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_wulfric.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_leader_wulfric.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_wulfric"))),

    DEFEAT_WIKSTROM(Advancement.Builder.createUntelemetered()
            .parent(DataGenerator.AdvancementProvider.ROOT)
            .criterion("defeat_elite_wikstrom", new DefeatTrainerCriterion.TrainerCountConditions("entity/elite_wikstrom"))
            .display(
                    XyTokenItem.ELITE_WIKSTROM_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_wikstrom.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_wikstrom.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_wikstrom"))),

    DEFEAT_MALVA(Advancement.Builder.createUntelemetered()
            .parent(XyAdvancement.DEFEAT_WIKSTROM.getAdvancement())
            .criterion("defeat_elite_malva", new DefeatTrainerCriterion.TrainerCountConditions("entity/elite_malva"))
            .display(
                    XyTokenItem.ELITE_MALVA_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_malva.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_malva.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_malva"))),

    DEFEAT_DRASNA(Advancement.Builder.createUntelemetered()
            .parent(XyAdvancement.DEFEAT_MALVA.getAdvancement())
            .criterion("defeat_elite_drasna", new DefeatTrainerCriterion.TrainerCountConditions("entity/elite_drasna"))
            .display(
                    XyTokenItem.ELITE_DRASNA_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_drasna.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_drasna.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_drasna"))),

    DEFEAT_SIEBOLD(Advancement.Builder.createUntelemetered()
            .parent(XyAdvancement.DEFEAT_DRASNA.getAdvancement())
            .criterion("defeat_elite_siebold", new DefeatTrainerCriterion.TrainerCountConditions("entity/elite_siebold"))
            .display(
                    XyTokenItem.ELITE_SIEBOLD_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_siebold.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_elite_siebold.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_siebold"))),

    DEFEAT_DIANTHA(Advancement.Builder.createUntelemetered()
            .parent(XyAdvancement.DEFEAT_SIEBOLD.getAdvancement())
            .criterion("defeat_champion_diantha", new DefeatTrainerCriterion.TrainerCountConditions("entity/champion_diantha"))
            .display(
                    XyTokenItem.CHAMPION_DIANTHA_TOKEN.getItem(),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_champion_diantha.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.defeat_champion_diantha.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.GOAL,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_champion_diantha"))),

    KILL_DIANTHA(Advancement.Builder.createUntelemetered()
            .parent(XyAdvancement.DEFEAT_SIEBOLD.getAdvancement())
            .criterion("kill_champion_diantha", new KillTrainerCriterion.TrainerCountConditions("entity/champion_diantha"))
            .display(
                    Items.NETHERITE_SWORD,
                    Text.translatable("advancement.cobblemontrainerbattle.kill_champion_diantha.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.kill_champion_diantha.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "kill_champion_diantha")));

    private final Advancement advancement;

    XyAdvancement(Advancement advancement) {
        this.advancement = advancement;
    }
    
    @Override
    public Advancement getAdvancement() {
        return advancement;
    }
}
