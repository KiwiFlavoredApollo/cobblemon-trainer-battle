package kiwiapollo.cobblemontrainerbattle.datagen.advancement;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.entity.CustomEntityType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.PlayerInteractedWithEntityCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.function.Consumer;

public class AdvancementProvider extends FabricAdvancementProvider {
    public static final Identifier BACKGROUND = Identifier.tryParse("textures/gui/advancements/backgrounds/adventure.png");
    public static final PlayerInteractedWithEntityCriterion.Conditions CONDITIONS = new PlayerInteractedWithEntityCriterion.Conditions(
            LootContextPredicate.EMPTY,
            ItemPredicate.ANY,
            EntityPredicate.asLootContextPredicate(EntityPredicate.Builder.create().type(CustomEntityType.NEUTRAL_TRAINER).build())
    );
    public static final Advancement ROOT = Advancement.Builder.createUntelemetered()
            .criterion("root", AdvancementProvider.CONDITIONS)
            .display(
                    Registries.ITEM.get(Identifier.of("cobblemon", "link_cable")),
                    Text.translatable("advancements.cobblemontrainerbattle.root.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.root.description"),
                    AdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "root"));

    public AdvancementProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        consumer.accept(ROOT);

        Arrays.stream(DefeatTrainerAdvancement.values()).map(CustomAdvancement::getAdvancement).forEach(consumer);
        Arrays.stream(KillTrainerAdvancement.values()).map(CustomAdvancement::getAdvancement).forEach(consumer);
        Arrays.stream(InclementEmeraldAdvancement.values()).map(CustomAdvancement::getAdvancement).forEach(consumer);
        Arrays.stream(RadicalRedAdvancement.values()).map(CustomAdvancement::getAdvancement).forEach(consumer);
        Arrays.stream(XyAdvancement.values()).map(CustomAdvancement::getAdvancement).forEach(consumer);
        Arrays.stream(BdspAdvancement.values()).map(CustomAdvancement::getAdvancement).forEach(consumer);
    }
}
