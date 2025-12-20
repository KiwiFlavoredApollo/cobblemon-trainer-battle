package kiwiapollo.cobblemontrainerbattle.datagen.advancement;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.DefeatTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.advancement.KillTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.item.token.InclementEmeraldTokenItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class InclementEmeraldAdvancementProvider extends FabricAdvancementProvider {
    private static final Identifier BACKGROUND = Identifier.of(Identifier.DEFAULT_NAMESPACE, "textures/gui/advancements/backgrounds/adventure.png");
    
    public static final Advancement DEFEAT_LEADER_ROXANNE = Advancement.Builder.createUntelemetered()
            .parent(RootAdvancementProvider.ROOT)
            .criterion("defeat_leader_roxanne", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_roxanne")))
            .display(
                    InclementEmeraldTokenItem.LEADER_ROXANNE_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_roxanne.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_roxanne.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_roxanne"));

    public static final Advancement DEFEAT_LEADER_BRAWLY = Advancement.Builder.createUntelemetered()
            .parent(InclementEmeraldAdvancementProvider.DEFEAT_LEADER_ROXANNE)
            .criterion("defeat_leader_brawly", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_brawly")))
            .display(
                    InclementEmeraldTokenItem.LEADER_BRAWLY_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_brawly.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_brawly.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_brawly"));

    public static final Advancement DEFEAT_LEADER_WATTSON = Advancement.Builder.createUntelemetered()
            .parent(InclementEmeraldAdvancementProvider.DEFEAT_LEADER_BRAWLY)
            .criterion("defeat_leader_wattson", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_wattson")))
            .display(
                    InclementEmeraldTokenItem.LEADER_WATTSON_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_wattson.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_wattson.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_wattson"));

    public static final Advancement DEFEAT_LEADER_FLANNERY = Advancement.Builder.createUntelemetered()
            .parent(InclementEmeraldAdvancementProvider.DEFEAT_LEADER_WATTSON)
            .criterion("defeat_leader_flannery", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_flannery")))
            .display(
                    InclementEmeraldTokenItem.LEADER_FLANNERY_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_flannery.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_flannery.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_flannery"));

    public static final Advancement DEFEAT_LEADER_NORMAN = Advancement.Builder.createUntelemetered()
            .parent(InclementEmeraldAdvancementProvider.DEFEAT_LEADER_FLANNERY)
            .criterion("defeat_leader_norman", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_norman")))
            .display(
                    InclementEmeraldTokenItem.LEADER_NORMAN_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_norman.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_norman.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_norman"));

    public static final Advancement DEFEAT_LEADER_WINONA = Advancement.Builder.createUntelemetered()
            .parent(InclementEmeraldAdvancementProvider.DEFEAT_LEADER_NORMAN)
            .criterion("defeat_leader_winona", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_winona")))
            .display(
                    InclementEmeraldTokenItem.LEADER_WINONA_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_winona.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_winona.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_winona"));

    public static final Advancement DEFEAT_LEADER_TATE_AND_LIZA = Advancement.Builder.createUntelemetered()
            .parent(InclementEmeraldAdvancementProvider.DEFEAT_LEADER_WINONA)
            .criterion("defeat_leader_tate_and_liza", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_tate_and_liza")))
            .display(
                    InclementEmeraldTokenItem.LEADER_TATE_AND_LIZA_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_tate_and_liza.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_tate_and_liza.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_tate_and_liza"));

    public static final Advancement DEFEAT_LEADER_JUAN = Advancement.Builder.createUntelemetered()
            .parent(InclementEmeraldAdvancementProvider.DEFEAT_LEADER_TATE_AND_LIZA)
            .criterion("defeat_leader_juan", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/leader_juan")))
            .display(
                    InclementEmeraldTokenItem.LEADER_JUAN_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_juan.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_leader_juan.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_leader_juan"));

    public static final Advancement DEFEAT_ELITE_SIDNEY = Advancement.Builder.createUntelemetered()
            .parent(RootAdvancementProvider.ROOT)
            .criterion("defeat_elite_sidney", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_sidney")))
            .display(
                    InclementEmeraldTokenItem.ELITE_SIDNEY_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_sidney.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_sidney.description"),
                    BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_sidney"));

    public static final Advancement DEFEAT_ELITE_PHOEBE = Advancement.Builder.createUntelemetered()
            .parent(InclementEmeraldAdvancementProvider.DEFEAT_ELITE_SIDNEY)
            .criterion("defeat_elite_phoebe", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_phoebe")))
            .display(
                    InclementEmeraldTokenItem.ELITE_PHOEBE_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_phoebe.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_phoebe.description"),
                    BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_phoebe"));

    public static final Advancement DEFEAT_ELITE_GLACIA = Advancement.Builder.createUntelemetered()
            .parent(InclementEmeraldAdvancementProvider.DEFEAT_ELITE_PHOEBE)
            .criterion("defeat_elite_glacia", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_glacia")))
            .display(
                    InclementEmeraldTokenItem.ELITE_GLACIA_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_glacia.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_glacia.description"),
                    BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_glacia"));

    public static final Advancement DEFEAT_ELITE_DRAKE = Advancement.Builder.createUntelemetered()
            .parent(InclementEmeraldAdvancementProvider.DEFEAT_ELITE_GLACIA)
            .criterion("defeat_elite_drake", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/elite_drake")))
            .display(
                    InclementEmeraldTokenItem.ELITE_DRAKE_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_drake.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_elite_drake.description"),
                    BACKGROUND,
                    AdvancementFrame.CHALLENGE,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_elite_drake"));

    public static final Advancement DEFEAT_CHAMPION_WALLACE = Advancement.Builder.createUntelemetered()
            .parent(InclementEmeraldAdvancementProvider.DEFEAT_ELITE_DRAKE)
            .criterion("defeat_champion_wallace", new DefeatTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/champion_wallace")))
            .display(
                    InclementEmeraldTokenItem.CHAMPION_WALLACE_TOKEN,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_champion_wallace.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_champion_wallace.description"),
                    BACKGROUND,
                    AdvancementFrame.GOAL,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_champion_wallace"));

    public static final Advancement KILL_CHAMPION_WALLACE = Advancement.Builder.createUntelemetered()
            .parent(InclementEmeraldAdvancementProvider.DEFEAT_ELITE_DRAKE)
            .criterion("kill_champion_wallace", new KillTrainerCriterion.TrainerCountConditions(Identifier.of(CobblemonTrainerBattle.MOD_ID, "entity/champion_wallace")))
            .display(
                    Items.NETHERITE_SWORD,
                    Text.translatable("advancements.cobblemontrainerbattle.kill_champion_wallace.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.kill_champion_wallace.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "kill_champion_wallace"));

    protected InclementEmeraldAdvancementProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        consumer.accept(DEFEAT_LEADER_ROXANNE);
        consumer.accept(DEFEAT_LEADER_BRAWLY);
        consumer.accept(DEFEAT_LEADER_WATTSON);
        consumer.accept(DEFEAT_LEADER_FLANNERY);
        consumer.accept(DEFEAT_LEADER_NORMAN);
        consumer.accept(DEFEAT_LEADER_WINONA);
        consumer.accept(DEFEAT_LEADER_TATE_AND_LIZA);
        consumer.accept(DEFEAT_LEADER_JUAN);
        consumer.accept(DEFEAT_ELITE_SIDNEY);
        consumer.accept(DEFEAT_ELITE_PHOEBE);
        consumer.accept(DEFEAT_ELITE_GLACIA);
        consumer.accept(DEFEAT_ELITE_DRAKE);
        consumer.accept(DEFEAT_CHAMPION_WALLACE);
        consumer.accept(KILL_CHAMPION_WALLACE);
    }
}
