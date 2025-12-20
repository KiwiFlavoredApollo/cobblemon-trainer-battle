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

import java.util.function.Consumer;

public class RootAdvancementProvider extends FabricAdvancementProvider {
    public static final Identifier BACKGROUND = Identifier.of(Identifier.DEFAULT_NAMESPACE, "textures/gui/advancements/backgrounds/adventure.png");

    private static final PlayerInteractedWithEntityCriterion.Conditions CONDITIONS = new PlayerInteractedWithEntityCriterion.Conditions(
            LootContextPredicate.EMPTY,
            ItemPredicate.ANY,
            EntityPredicate.asLootContextPredicate(EntityPredicate.Builder.create().type(CustomEntityType.TRAINER).build())
    );

    public static final Advancement ROOT = Advancement.Builder.createUntelemetered()
            .criterion("root", RootAdvancementProvider.CONDITIONS)
            .display(
                    Registries.ITEM.get(Identifier.of("cobblemon", "link_cable")),
                    Text.translatable("advancements.cobblemontrainerbattle.root.title"),
                    Text.translatable("advancements.cobblemontrainerbattle.root.description"),
                    RootAdvancementProvider.BACKGROUND,
                    AdvancementFrame.TASK,
                    true,
                    true,
                    false
            )
            .build(Identifier.of(CobblemonTrainerBattle.MOD_ID, "root"));

    public RootAdvancementProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        consumer.accept(ROOT);
    }
}
