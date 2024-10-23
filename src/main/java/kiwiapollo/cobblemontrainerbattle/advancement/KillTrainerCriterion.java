package kiwiapollo.cobblemontrainerbattle.advancement;

import com.cobblemon.mod.common.advancement.criterion.CountableContext;
import com.cobblemon.mod.common.advancement.criterion.SimpleCountableCriterionCondition;
import com.google.gson.JsonObject;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.entities.TrainerEntity;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class KillTrainerCriterion extends AbstractCriterion<KillTrainerCriterion.Conditions> {
    private static final Identifier ID = Identifier.of(CobblemonTrainerBattle.NAMESPACE, "kill_trainer");

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    protected KillTrainerCriterion.Conditions conditionsFromJson(
            JsonObject obj,
            LootContextPredicate playerPredicate,
            AdvancementEntityPredicateDeserializer predicateDeserializer
    ) {
        return new KillTrainerCriterion.Conditions(obj.get("count").getAsInt());
    }

    public void trigger(ServerPlayerEntity player, TrainerEntity trainer, DamageSource damageSource) {
        int count = CobblemonTrainerBattle.playerHistoryRegistry.get(player.getUuid()).getTotalKillCount();
        trigger(player, conditions -> conditions.test(player, trainer, damageSource, count));
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final SimpleCountableCriterionCondition countConditions;
        private final OnKilledCriterion.Conditions entityCondition;

        public Conditions(int count) {
            super(ID, LootContextPredicate.EMPTY);

            this.entityCondition = OnKilledCriterion.Conditions.createPlayerKilledEntity(
                    new EntityPredicate.Builder().type(CobblemonTrainerBattle.TRAINER_ENTITY_TYPE).build()
            );

            this.countConditions = new SimpleCountableCriterionCondition(ID, LootContextPredicate.EMPTY);
            this.countConditions.setCount(count);
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            return countConditions.toJson(predicateSerializer);
        }

        boolean test(ServerPlayerEntity player, TrainerEntity trainer, DamageSource damageSource, int count) {
            LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, trainer);
            return countConditions.matches(player, new CountableContext(count))
                    && entityCondition.test(player, lootContext, damageSource);
        }
    }
}
