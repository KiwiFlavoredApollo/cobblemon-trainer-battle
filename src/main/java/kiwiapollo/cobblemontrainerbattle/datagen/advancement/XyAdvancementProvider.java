package kiwiapollo.cobblemontrainerbattle.datagen.advancement;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.DefeatTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.advancement.KillTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.item.token.XyTokenItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class XyAdvancementProvider extends FabricAdvancementProvider {
    private static final Identifier BACKGROUND = Identifier.of(Identifier.DEFAULT_NAMESPACE, "textures/gui/advancements/backgrounds/adventure.png");
    
    public static final Advancement DEFEAT_LEADER_VIOLA = Advancement.Builder.createUntelemetered()
            .parent(RootAdvancementProvider.ROOT)
            .criterion("defeat_leader_viola", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_viola")))
            .display(
                    XyTokenItem.LEADER_VIOLA_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_viola.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_viola.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_viola"));

    public static final Advancement DEFEAT_LEADER_GRANT = Advancement.Builder.createUntelemetered()
            .parent(XyAdvancementProvider.DEFEAT_LEADER_VIOLA)
            .criterion("defeat_leader_grant", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_grant")))
            .display(
                    XyTokenItem.LEADER_GRANT_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_grant.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_grant.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_grant"));

    public static final Advancement DEFEAT_LEADER_KORRINA = Advancement.Builder.createUntelemetered()
            .parent(XyAdvancementProvider.DEFEAT_LEADER_GRANT)
            .criterion("defeat_leader_korrina", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_korrina")))
            .display(
                    XyTokenItem.LEADER_KORRINA_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_korrina.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_korrina.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_korrina"));

    public static final Advancement DEFEAT_LEADER_RAMOS = Advancement.Builder.createUntelemetered()
            .parent(XyAdvancementProvider.DEFEAT_LEADER_KORRINA)
            .criterion("defeat_leader_ramos", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_ramos")))
            .display(
                    XyTokenItem.LEADER_RAMOS_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_ramos.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_ramos.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_ramos"));

    public static final Advancement DEFEAT_LEADER_CLEMONT = Advancement.Builder.createUntelemetered()
            .parent(XyAdvancementProvider.DEFEAT_LEADER_RAMOS)
            .criterion("defeat_leader_clemont", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_clemont")))
            .display(
                    XyTokenItem.LEADER_CLEMONT_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_clemont.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_clemont.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_clemont"));

    public static final Advancement DEFEAT_LEADER_VALERIE = Advancement.Builder.createUntelemetered()
            .parent(XyAdvancementProvider.DEFEAT_LEADER_CLEMONT)
            .criterion("defeat_leader_valerie", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_valerie")))
            .display(
                    XyTokenItem.LEADER_VALERIE_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_valerie.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_valerie.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_valerie"));

    public static final Advancement DEFEAT_LEADER_OLYMPIA = Advancement.Builder.createUntelemetered()
            .parent(XyAdvancementProvider.DEFEAT_LEADER_VALERIE)
            .criterion("defeat_leader_olympia", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_olympia")))
            .display(
                    XyTokenItem.LEADER_OLYMPIA_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_olympia.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_olympia.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_olympia"));

    public static final Advancement DEFEAT_LEADER_WULFRIC = Advancement.Builder.createUntelemetered()
            .parent(XyAdvancementProvider.DEFEAT_LEADER_OLYMPIA)
            .criterion("defeat_leader_wulfric", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_wulfric")))
            .display(
                    XyTokenItem.LEADER_WULFRIC_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_wulfric.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_wulfric.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_wulfric"));

    public static final Advancement DEFEAT_ELITE_WIKSTROM = Advancement.Builder.createUntelemetered()
            .parent(RootAdvancementProvider.ROOT)
            .criterion("defeat_elite_wikstrom", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_wikstrom")))
            .display(
                    XyTokenItem.ELITE_WIKSTROM_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_wikstrom.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_wikstrom.description"),
                    BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_wikstrom"));

    public static final Advancement DEFEAT_ELITE_MALVA = Advancement.Builder.createUntelemetered()
            .parent(XyAdvancementProvider.DEFEAT_ELITE_WIKSTROM)
            .criterion("defeat_elite_malva", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_malva")))
            .display(
                    XyTokenItem.ELITE_MALVA_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_malva.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_malva.description"),
                    BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_malva"));

    public static final Advancement DEFEAT_ELITE_DRASNA = Advancement.Builder.createUntelemetered()
            .parent(XyAdvancementProvider.DEFEAT_ELITE_MALVA)
            .criterion("defeat_elite_drasna", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_drasna")))
            .display(
                    XyTokenItem.ELITE_DRASNA_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_drasna.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_drasna.description"),
                    BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_drasna"));

    public static final Advancement DEFEAT_ELITE_SIEBOLD = Advancement.Builder.createUntelemetered()
            .parent(XyAdvancementProvider.DEFEAT_ELITE_DRASNA)
            .criterion("defeat_elite_siebold", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_siebold")))
            .display(
                    XyTokenItem.ELITE_SIEBOLD_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_siebold.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_siebold.description"),
                    BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_siebold"));

    public static final Advancement DEFEAT_CHAMPION_DIANTHA = Advancement.Builder.createUntelemetered()
            .parent(XyAdvancementProvider.DEFEAT_ELITE_SIEBOLD)
            .criterion("defeat_champion_diantha", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/champion_diantha")))
            .display(
                    XyTokenItem.CHAMPION_DIANTHA_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_champion_diantha.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_champion_diantha.description"),
                    BACKGROUND,
                    AdvancementFrame.GOAL,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_champion_diantha"));

    public static final Advancement KILL_CHAMPION_DIANTHA = Advancement.Builder.createUntelemetered()
            .parent(XyAdvancementProvider.DEFEAT_ELITE_SIEBOLD)
            .criterion("kill_champion_diantha", new KillTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/champion_diantha")))
            .display(
                    Items.NETHERITE_SWORD,
                    Text.translatable("advancements.cobblemontrainerbattle.kill_champion_diantha.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.kill_champion_diantha.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "kill_champion_diantha"));

    protected XyAdvancementProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        consumer.accept(DEFEAT_LEADER_VIOLA);
        consumer.accept(DEFEAT_LEADER_GRANT);
        consumer.accept(DEFEAT_LEADER_KORRINA);
        consumer.accept(DEFEAT_LEADER_RAMOS);
        consumer.accept(DEFEAT_LEADER_CLEMONT);
        consumer.accept(DEFEAT_LEADER_VALERIE);
        consumer.accept(DEFEAT_LEADER_OLYMPIA);
        consumer.accept(DEFEAT_LEADER_WULFRIC);
        consumer.accept(DEFEAT_ELITE_WIKSTROM);
        consumer.accept(DEFEAT_ELITE_MALVA);
        consumer.accept(DEFEAT_ELITE_DRASNA);
        consumer.accept(DEFEAT_ELITE_SIEBOLD);
        consumer.accept(DEFEAT_CHAMPION_DIANTHA);
        consumer.accept(KILL_CHAMPION_DIANTHA);
    }
}
