package kiwiapollo.cobblemontrainerbattle.advancement;

import com.google.gson.JsonObject;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.parser.history.PlayerHistoryManager;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class KillTrainerCriterion extends AbstractCriterion<KillTrainerCriterion.Conditions> {
    private static final Identifier ID = Identifier.of(CobblemonTrainerBattle.MOD_ID, "kill_trainer");

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
        Identifier trainer = null;
        if (obj.has("trainer")) {
            trainer = Identifier.tryParse(obj.get("trainer").getAsString());
        }

        Integer count = null;
        if (obj.has("count")) {
            count = obj.get("count").getAsInt();
        }

        return new KillTrainerCriterion.Conditions(trainer, count);
    }

    public void trigger(ServerPlayerEntity player) {
        trigger(player, conditions -> conditions.test(player));
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final Identifier trainer;
        private final Integer count;

        public Conditions(Identifier trainer) {
            this(trainer, null);
        }

        public Conditions(Integer count) {
            this(null, count);
        }

        public Conditions(Identifier trainer, Integer count) {
            super(ID, LootContextPredicate.EMPTY);

            this.trainer = trainer;
            this.count = count;
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = new JsonObject();

            if (Objects.nonNull(trainer)) {
                jsonObject.addProperty("trainer", trainer.toString());
            }

            if (Objects.nonNull(count)) {
                jsonObject.addProperty("count", count);
            }

            return jsonObject;
        }

        boolean test(ServerPlayerEntity player) {
            if (Objects.isNull(trainer)) {
                return testTotalKillCount(player);

            } else {
                return testTrainerKillCount(player);
            }
        }

        private boolean testTotalKillCount(ServerPlayerEntity player) {
            try {
                return count <= PlayerHistoryManager.get(player.getUuid()).getTotalKillCount();
            } catch (NullPointerException e) {
                return 0 < PlayerHistoryManager.get(player.getUuid()).getTotalKillCount();
            }
        }

        private boolean testTrainerKillCount(ServerPlayerEntity player) {
            try {
                return count <= PlayerHistoryManager.get(player.getUuid()).getTrainerKillCount(trainer);
            } catch (NullPointerException e) {
                return 0 < PlayerHistoryManager.get(player.getUuid()).getTrainerKillCount(trainer);
            }
        }
    }
}
