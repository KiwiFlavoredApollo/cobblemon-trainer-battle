package kiwiapollo.cobblemontrainerbattle.datagen.advancement;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.DefeatTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.advancement.KillTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.item.token.BdspTokenItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class BdspAdvancementProvider extends FabricAdvancementProvider {
    private static final Identifier BACKGROUND = Identifier.of(Identifier.DEFAULT_NAMESPACE, "textures/gui/advancements/backgrounds/adventure.png");
    
    public static final Advancement DEFEAT_LEADER_ROARK = Advancement.Builder.createUntelemetered()
            .parent(RootAdvancementProvider.ROOT)
            .criterion("defeat_leader_roark", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_roark")))
            .display(
                    BdspTokenItem.LEADER_ROARK_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_roark.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_roark.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_roark"));

    public static final Advancement DEFEAT_LEADER_GARDENIA = Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancementProvider.DEFEAT_LEADER_ROARK)
            .criterion("defeat_leader_gardenia", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_gardenia")))
            .display(
                    BdspTokenItem.LEADER_GARDENIA_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_gardenia.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_gardenia.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_gardenia"));

    public static final Advancement DEFEAT_LEADER_MAYLENE = Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancementProvider.DEFEAT_LEADER_GARDENIA)
            .criterion("defeat_leader_maylene", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_maylene")))
            .display(
                    BdspTokenItem.LEADER_MAYLENE_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_maylene.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_maylene.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_maylene"));

    public static final Advancement DEFEAT_LEADER_CRASHER_WAKE = Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancementProvider.DEFEAT_LEADER_MAYLENE)
            .criterion("defeat_leader_crasher_wake", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_crasher_wake")))
            .display(
                    BdspTokenItem.LEADER_CRASHER_WAKE_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_crasher_wake.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_crasher_wake.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_crasher_wake"));

    public static final Advancement DEFEAT_LEADER_FANTINA = Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancementProvider.DEFEAT_LEADER_CRASHER_WAKE)
            .criterion("defeat_leader_fantina", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_fantina")))
            .display(
                    BdspTokenItem.LEADER_FANTINA_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_fantina.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_fantina.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_fantina"));

    public static final Advancement DEFEAT_LEADER_BYRON = Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancementProvider.DEFEAT_LEADER_FANTINA)
            .criterion("defeat_leader_byron", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_byron")))
            .display(
                    BdspTokenItem.LEADER_BYRON_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_byron.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_byron.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_byron"));

    public static final Advancement DEFEAT_LEADER_CANDICE = Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancementProvider.DEFEAT_LEADER_BYRON)
            .criterion("defeat_leader_candice", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_candice")))
            .display(
                    BdspTokenItem.LEADER_CANDICE_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_candice.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_candice.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_candice"));

    public static final Advancement DEFEAT_LEADER_VOLKNER = Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancementProvider.DEFEAT_LEADER_CANDICE)
            .criterion("defeat_leader_volkner", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_volkner")))
            .display(
                    BdspTokenItem.LEADER_VOLKNER_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_volkner.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_volkner.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_volkner"));

    public static final Advancement DEFEAT_ELITE_AARON = Advancement.Builder.createUntelemetered()
            .parent(RootAdvancementProvider.ROOT)
            .criterion("defeat_elite_aaron", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_aaron")))
            .display(
                    BdspTokenItem.ELITE_AARON_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_aaron.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_aaron.description"),
                    BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_aaron"));

    public static final Advancement DEFEAT_ELITE_BERTHA = Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancementProvider.DEFEAT_ELITE_AARON)
            .criterion("defeat_elite_bertha", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_bertha")))
            .display(
                    BdspTokenItem.ELITE_BERTHA_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_bertha.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_bertha.description"),
                    BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_bertha"));

    public static final Advancement DEFEAT_ELITE_FLINT = Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancementProvider.DEFEAT_ELITE_BERTHA)
            .criterion("defeat_elite_flint", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_flint")))
            .display(
                    BdspTokenItem.ELITE_FLINT_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_flint.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_flint.description"),
                    BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_flint"));

    public static final Advancement DEFEAT_ELITE_LUCIAN = Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancementProvider.DEFEAT_ELITE_FLINT)
            .criterion("defeat_elite_lucian", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_lucian")))
            .display(
                    BdspTokenItem.ELITE_LUCIAN_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_lucian.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_lucian.description"),
                    BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_lucian"));

    public static final Advancement DEFEAT_CHAMPION_CYNTHIA = Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancementProvider.DEFEAT_ELITE_LUCIAN)
            .criterion("defeat_champion_cynthia", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/champion_cynthia")))
            .display(
                    BdspTokenItem.CHAMPION_CYNTHIA_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_champion_cynthia.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_champion_cynthia.description"),
                    BACKGROUND,
                    AdvancementFrame.GOAL,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_champion_cynthia"));

    public static final Advancement KILL_CHAMPION_CYNTHIA = Advancement.Builder.createUntelemetered()
            .parent(BdspAdvancementProvider.DEFEAT_ELITE_LUCIAN)
            .criterion("kill_champion_cynthia", new KillTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/champion_cynthia")))
            .display(
                    Items.NETHERITE_SWORD,
                    Text.translatable("advancements.cobblemontrainerbattle.kill_champion_cynthia.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.kill_champion_cynthia.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "kill_champion_cynthia"));

    protected BdspAdvancementProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        consumer.accept(DEFEAT_LEADER_ROARK);
        consumer.accept(DEFEAT_LEADER_GARDENIA);
        consumer.accept(DEFEAT_LEADER_MAYLENE);
        consumer.accept(DEFEAT_LEADER_CRASHER_WAKE);
        consumer.accept(DEFEAT_LEADER_FANTINA);
        consumer.accept(DEFEAT_LEADER_BYRON);
        consumer.accept(DEFEAT_LEADER_CANDICE);
        consumer.accept(DEFEAT_LEADER_VOLKNER);
        consumer.accept(DEFEAT_ELITE_AARON);
        consumer.accept(DEFEAT_ELITE_BERTHA);
        consumer.accept(DEFEAT_ELITE_FLINT);
        consumer.accept(DEFEAT_ELITE_LUCIAN);
        consumer.accept(DEFEAT_CHAMPION_CYNTHIA);
        consumer.accept(KILL_CHAMPION_CYNTHIA);
    }
}
