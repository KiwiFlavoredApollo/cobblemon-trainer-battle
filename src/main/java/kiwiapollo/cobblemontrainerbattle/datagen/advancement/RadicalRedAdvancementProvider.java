package kiwiapollo.cobblemontrainerbattle.datagen.advancement;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.DefeatTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.advancement.KillTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.item.token.RadicalRedTokenItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class RadicalRedAdvancementProvider extends FabricAdvancementProvider {
    public static final Identifier BACKGROUND = Identifier.of(Identifier.DEFAULT_NAMESPACE, "textures/gui/advancements/backgrounds/adventure.png");
    
    public static final Advancement DEFEAT_LEADER_BROCK = Advancement.Builder.createUntelemetered()
            .parent(RootAdvancementProvider.ROOT)
            .criterion("defeat_leader_brock", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_brock")))
            .display(
                    RadicalRedTokenItem.LEADER_BROCK_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_brock.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_brock.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_brock"));

    public static final Advancement DEFEAT_LEADER_MISTY = Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancementProvider.DEFEAT_LEADER_BROCK)
            .criterion("defeat_leader_misty", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_misty")))
            .display(
                    RadicalRedTokenItem.LEADER_MISTY_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_misty.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_misty.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_misty"));

    public static final Advancement DEFEAT_LEADER_LT_SURGE = Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancementProvider.DEFEAT_LEADER_MISTY)
            .criterion("defeat_leader_lt_surge", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_lt_surge")))
            .display(
                    RadicalRedTokenItem.LEADER_LT_SURGE_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_lt_surge.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_lt_surge.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_lt_surge"));

    public static final Advancement DEFEAT_LEADER_ERIKA = Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancementProvider.DEFEAT_LEADER_LT_SURGE)
            .criterion("defeat_leader_erika", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_erika")))
            .display(
                    RadicalRedTokenItem.LEADER_ERIKA_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_erika.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_erika.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_erika"));

    public static final Advancement DEFEAT_LEADER_KOGA = Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancementProvider.DEFEAT_LEADER_ERIKA)
            .criterion("defeat_leader_koga", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_koga")))
            .display(
                    RadicalRedTokenItem.LEADER_KOGA_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_koga.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_koga.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_koga"));

    public static final Advancement DEFEAT_LEADER_SABRINA = Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancementProvider.DEFEAT_LEADER_KOGA)
            .criterion("defeat_leader_sabrina", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_sabrina")))
            .display(
                    RadicalRedTokenItem.LEADER_SABRINA_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_sabrina.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_sabrina.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_sabrina"));

    public static final Advancement DEFEAT_LEADER_BLAINE = Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancementProvider.DEFEAT_LEADER_SABRINA)
            .criterion("defeat_leader_blaine", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_blaine")))
            .display(
                    RadicalRedTokenItem.LEADER_BLAINE_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_blaine.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_blaine.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_blaine"));

    public static final Advancement DEFEAT_LEADER_GIOVANNI = Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancementProvider.DEFEAT_LEADER_BLAINE)
            .criterion("defeat_leader_giovanni", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_giovanni")))
            .display(
                    RadicalRedTokenItem.LEADER_GIOVANNI_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_giovanni.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_giovanni.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_giovanni"));

    public static final Advancement DEFEAT_LEADER_FALKNER = Advancement.Builder.createUntelemetered()
            .parent(RootAdvancementProvider.ROOT)
            .criterion("defeat_leader_falkner", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_falkner")))
            .display(
                    RadicalRedTokenItem.LEADER_FALKNER_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_falkner.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_falkner.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_falkner"));

    public static final Advancement DEFEAT_LEADER_BUGSY = Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancementProvider.DEFEAT_LEADER_FALKNER)
            .criterion("defeat_leader_bugsy", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_bugsy")))
            .display(
                    RadicalRedTokenItem.LEADER_BUGSY_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_bugsy.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_bugsy.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_bugsy"));

    public static final Advancement DEFEAT_LEADER_WHITNEY = Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancementProvider.DEFEAT_LEADER_BUGSY)
            .criterion("defeat_leader_whitney", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_whitney")))
            .display(
                    RadicalRedTokenItem.LEADER_WHITNEY_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_whitney.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_whitney.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_whitney"));

    public static final Advancement DEFEAT_LEADER_MORTY = Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancementProvider.DEFEAT_LEADER_WHITNEY)
            .criterion("defeat_leader_morty", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_morty")))
            .display(
                    RadicalRedTokenItem.LEADER_MORTY_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_morty.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_morty.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_morty"));

    public static final Advancement DEFEAT_LEADER_CHUCK = Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancementProvider.DEFEAT_LEADER_MORTY)
            .criterion("defeat_leader_chuck", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_chuck")))
            .display(
                    RadicalRedTokenItem.LEADER_CHUCK_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_chuck.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_chuck.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_chuck"));

    public static final Advancement DEFEAT_LEADER_JASMINE = Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancementProvider.DEFEAT_LEADER_CHUCK)
            .criterion("defeat_leader_jasmine", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_jasmine")))
            .display(
                    RadicalRedTokenItem.LEADER_JASMINE_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_jasmine.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_jasmine.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_jasmine"));

    public static final Advancement DEFEAT_LEADER_PRYCE = Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancementProvider.DEFEAT_LEADER_JASMINE)
            .criterion("defeat_leader_pryce", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_pryce")))
            .display(
                    RadicalRedTokenItem.LEADER_PRYCE_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_pryce.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_pryce.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_pryce"));

    public static final Advancement DEFEAT_LEADER_CLAIR = Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancementProvider.DEFEAT_LEADER_PRYCE)
            .criterion("defeat_leader_clair", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_clair")))
            .display(
                    RadicalRedTokenItem.LEADER_CLAIR_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_clair.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_clair.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_clair"));

    public static final Advancement DEFEAT_ELITE_LORELEI = Advancement.Builder.createUntelemetered()
            .parent(RootAdvancementProvider.ROOT)
            .criterion("defeat_elite_lorelei", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_lorelei")))
            .display(
                    RadicalRedTokenItem.ELITE_LORELEI_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_lorelei.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_lorelei.description"),
                    BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_lorelei"));

    public static final Advancement DEFEAT_ELITE_BRUNO = Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancementProvider.DEFEAT_ELITE_LORELEI)
            .criterion("defeat_elite_bruno", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_bruno")))
            .display(
                    RadicalRedTokenItem.ELITE_BRUNO_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_bruno.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_bruno.description"),
                    BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_bruno"));

    public static final Advancement DEFEAT_ELITE_AGATHA = Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancementProvider.DEFEAT_ELITE_BRUNO)
            .criterion("defeat_elite_agatha", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_agatha")))
            .display(
                    RadicalRedTokenItem.ELITE_AGATHA_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_agatha.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_agatha.description"),
                    BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_agatha"));

    public static final Advancement DEFEAT_ELITE_LANCE = Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancementProvider.DEFEAT_ELITE_AGATHA)
            .criterion("defeat_elite_lance", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_lance")))
            .display(
                    RadicalRedTokenItem.ELITE_LANCE_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_lance.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_lance.description"),
                    BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_lance"));

    public static final Advancement DEFEAT_CHAMPION_TERRY = Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancementProvider.DEFEAT_ELITE_LANCE)
            .criterion("defeat_champion_terry", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/champion_terry")))
            .display(
                    RadicalRedTokenItem.CHAMPION_TERRY_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_champion_terry.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_champion_terry.description"),
                    BACKGROUND,
                    AdvancementFrame.GOAL,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_champion_terry"));

    public static final Advancement KILL_CHAMPION_TERRY = Advancement.Builder.createUntelemetered()
            .parent(RadicalRedAdvancementProvider.DEFEAT_ELITE_LANCE)
            .criterion("kill_champion_terry", new KillTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/champion_terry")))
            .display(
                    Items.NETHERITE_SWORD,
                    Text.translatable("advancements.cobblemontrainerbattle.kill_champion_terry.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.kill_champion_terry.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "kill_champion_terry"));

    protected RadicalRedAdvancementProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        consumer.accept(DEFEAT_LEADER_BROCK);
        consumer.accept(DEFEAT_LEADER_MISTY);
        consumer.accept(DEFEAT_LEADER_LT_SURGE);
        consumer.accept(DEFEAT_LEADER_ERIKA);
        consumer.accept(DEFEAT_LEADER_KOGA);
        consumer.accept(DEFEAT_LEADER_SABRINA);
        consumer.accept(DEFEAT_LEADER_BLAINE);
        consumer.accept(DEFEAT_LEADER_GIOVANNI);
        consumer.accept(DEFEAT_LEADER_FALKNER);
        consumer.accept(DEFEAT_LEADER_BUGSY);
        consumer.accept(DEFEAT_LEADER_WHITNEY);
        consumer.accept(DEFEAT_LEADER_MORTY);
        consumer.accept(DEFEAT_LEADER_CHUCK);
        consumer.accept(DEFEAT_LEADER_JASMINE);
        consumer.accept(DEFEAT_LEADER_PRYCE);
        consumer.accept(DEFEAT_LEADER_CLAIR);
        consumer.accept(DEFEAT_ELITE_LORELEI);
        consumer.accept(DEFEAT_ELITE_BRUNO);
        consumer.accept(DEFEAT_ELITE_AGATHA);
        consumer.accept(DEFEAT_ELITE_LANCE);
        consumer.accept(DEFEAT_CHAMPION_TERRY);
        consumer.accept(KILL_CHAMPION_TERRY);
    }
}
