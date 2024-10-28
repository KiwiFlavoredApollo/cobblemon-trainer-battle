package kiwiapollo.cobblemontrainerbattle.advancement;

import com.google.gson.JsonObject;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.entities.EntityTypes;
import kiwiapollo.cobblemontrainerbattle.entities.TrainerEntity;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.advancement.criterion.PlayerInteractedWithEntityCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class InteractTrainerCriterion extends AbstractCriterion<InteractTrainerCriterion.Conditions> {
    private static final Identifier ID = Identifier.of(CobblemonTrainerBattle.NAMESPACE, "interact_trainer");

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    protected Conditions conditionsFromJson(
            JsonObject obj,
            LootContextPredicate playerPredicate,
            AdvancementEntityPredicateDeserializer predicateDeserializer
    ) {
        return new Conditions();
    }

    public void trigger(ServerPlayerEntity player, ItemStack itemStack, TrainerEntity trainer) {
        trigger(player, conditions -> conditions.test(player, itemStack, trainer));
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final PlayerInteractedWithEntityCriterion.Conditions conditions;

        public Conditions() {
            super(ID, LootContextPredicate.EMPTY);
            this.conditions = PlayerInteractedWithEntityCriterion.Conditions.create(
                    ItemPredicate.Builder.create().items(Items.AIR),
                    EntityPredicate.asLootContextPredicate(
                            EntityPredicate.Builder.create().type(EntityTypes.TRAINER).build()
                    )
            );
        }

        boolean test(ServerPlayerEntity player, ItemStack itemStack, TrainerEntity trainer) {
            LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, trainer);
            return conditions.test(itemStack, lootContext);
        }
    }
}
