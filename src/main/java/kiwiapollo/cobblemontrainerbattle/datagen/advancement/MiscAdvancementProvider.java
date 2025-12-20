package kiwiapollo.cobblemontrainerbattle.datagen.advancement;

import com.cobblemon.mod.common.CobblemonItems;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.DefeatTrainerCriterion;
import kiwiapollo.cobblemontrainerbattle.advancement.KillTrainerCriterion;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class MiscAdvancementProvider extends FabricAdvancementProvider {
    private static final Identifier BACKGROUND = Identifier.of(Identifier.DEFAULT_NAMESPACE, "textures/gui/advancements/backgrounds/adventure.png");

    private static final Advancement DEFEAT_FIRST_TRAINER = Advancement.Builder.createUntelemetered()
            .parent(RootAdvancementProvider.ROOT)
            .criterion("defeat_first_trainer", new DefeatTrainerCriterion.TotalCountConditions(1))
            .display(
                    CobblemonItems.POKE_BALL,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_first_trainer.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_first_trainer.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_first_trainer"));

    private static final Advancement DEFEAT_TENTH_TRAINER = Advancement.Builder.createUntelemetered()
            .parent(MiscAdvancementProvider.DEFEAT_FIRST_TRAINER)
            .criterion("defeat_tenth_trainer", new DefeatTrainerCriterion.TotalCountConditions(10))
            .display(
                    CobblemonItems.GREAT_BALL,
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_tenth_trainer.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.defeat_tenth_trainer.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "defeat_tenth_trainer"));

    private static final Advancement KILL_FIRST_TRAINER = Advancement.Builder.createUntelemetered()
            .parent(RootAdvancementProvider.ROOT)
            .criterion("kill_first_trainer", new KillTrainerCriterion.TotalCountConditions(1))
            .display(
                    Items.STONE_SWORD,
                    Text.translatable("advancements.cobblemontrainerbattle.kill_first_trainer.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.kill_first_trainer.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "kill_first_trainer"));

    private static final Advancement KILL_TENTH_TRAINER = Advancement.Builder.createUntelemetered()
            .parent(KILL_FIRST_TRAINER)
            .criterion("kill_tenth_trainer", new KillTrainerCriterion.TotalCountConditions(10))
            .display(
                    Items.IRON_SWORD,
                    Text.translatable("advancements.cobblemontrainerbattle.kill_tenth_trainer.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.kill_tenth_trainer.description"),
                    BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "kill_tenth_trainer"));

    protected MiscAdvancementProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        consumer.accept(DEFEAT_FIRST_TRAINER);
        consumer.accept(DEFEAT_TENTH_TRAINER);
        consumer.accept(KILL_FIRST_TRAINER);
        consumer.accept(KILL_TENTH_TRAINER);
    }
}
