package kiwiapollo.cobblemontrainerbattle.advancement;

import com.cobblemon.mod.common.advancement.criterion.CountableContext;
import com.cobblemon.mod.common.advancement.criterion.SimpleCountableCriterionCondition;
import com.google.gson.JsonObject;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class DefeatTrainerCriterion extends AbstractCriterion<DefeatTrainerCriterion.Conditions> {
    private static final Identifier ID = Identifier.of(CobblemonTrainerBattle.NAMESPACE, "defeat_trainer");

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
        return new Conditions(obj.get("count").getAsInt());
    }

    public void trigger(ServerPlayerEntity player) {
        int count = CobblemonTrainerBattle.playerHistoryRegistry.get(player.getUuid()).getTotalVictoryCount();
        trigger(player, conditions -> conditions.test(player, count));
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final SimpleCountableCriterionCondition conditions;

        public Conditions(int count) {
            super(ID, LootContextPredicate.EMPTY);

            this.conditions = new SimpleCountableCriterionCondition(ID, LootContextPredicate.EMPTY);
            this.conditions.setCount(count);
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            return conditions.toJson(predicateSerializer);
        }

        boolean test(ServerPlayerEntity player, int count) {
            return conditions.matches(player, new CountableContext(count));
        }
    }
}
