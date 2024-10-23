package kiwiapollo.cobblemontrainerbattle.advancement;

import com.cobblemon.mod.common.advancement.criterion.CountableCriterionCondition;
import com.cobblemon.mod.common.advancement.criterion.CountableCriterionKt;
import com.cobblemon.mod.common.advancement.criterion.SimpleCountableCriterionCondition;
import com.google.gson.JsonObject;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
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
        return new Conditions();
    }

    public void trigger(ServerPlayerEntity player) {
        trigger(player, Conditions::test);
    }

    public static class Conditions extends AbstractCriterionConditions {

        public Conditions() {
            super(ID, LootContextPredicate.EMPTY);
        }

        boolean test() {
            return true;
        }
    }
}
