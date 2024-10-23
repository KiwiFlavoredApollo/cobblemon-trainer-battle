package kiwiapollo.cobblemontrainerbattle.datagen;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class KillTrainerAdvancementFactory implements AdvancementFactory {
    private static final Advancement FIRST = Advancement.Builder.createUntelemetered()
            .parent(DataGenerator.AdvancementProvider.ROOT)
            .criterion("kill_first_trainer", OnKilledCriterion.Conditions.createPlayerKilledEntity(
                    new EntityPredicate.Builder().type(CobblemonTrainerBattle.TRAINER_ENTITY_TYPE).build()
            ))
            .display(
                    Items.STONE_SWORD,
                    Text.translatable("advancement.cobblemontrainerbattle.kill_trainer.first.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.kill_trainer.first.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.NAMESPACE, "kill_first_trainer"));

    private static final Advancement TENTH = Advancement.Builder.createUntelemetered()
            .parent(FIRST)
            .criterion("kill_tenth_trainer", OnKilledCriterion.Conditions.createPlayerKilledEntity(
                    new EntityPredicate.Builder().type(CobblemonTrainerBattle.TRAINER_ENTITY_TYPE).build()
            ))
            .display(
                    Items.IRON_SWORD,
                    Text.translatable("advancement.cobblemontrainerbattle.kill_trainer.tenth.title"),
                    Text.translatable("advancement.cobblemontrainerbattle.kill_trainer.tenth.description"),
                    DataGenerator.AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.NAMESPACE, "kill_tenth_trainer"));

    public KillTrainerAdvancementFactory() {

    }

    @Override
    public List<Advancement> create() {
        return List.of(
                FIRST,
                TENTH
        );
    }
}
