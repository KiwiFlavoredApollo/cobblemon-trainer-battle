package kiwiapollo.cobblemontrainerbattle.advancement;

import com.google.gson.JsonObject;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.parser.history.EntityRecord;
import kiwiapollo.cobblemontrainerbattle.parser.history.PlayerHistoryManager;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class KillTrainerCriterion extends AbstractCriterion<KillTrainerCriterion.Conditions> {
    private static final Identifier ID = Identifier.of(CobblemonTrainerBattle.MOD_ID, "kill_trainer");
    private static final int ONE = 1;

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
        if (isTrainerConditionExist(obj)) {
            return new KillTrainerCriterion.TrainerCountConditions(getTrainerCondition(obj), getCountCondition(obj));

        } else {
            return new KillTrainerCriterion.TotalCountConditions(getCountCondition(obj));
        }
    }

    private boolean isTrainerConditionExist(JsonObject obj) {
        return obj.has("trainer");
    }

    private Identifier getTrainerCondition(JsonObject obj) {
        return Identifier.tryParse(obj.get("trainer").getAsString());
    }

    private int getCountCondition(JsonObject obj) {
        return obj.has("count") ? obj.get("count").getAsInt() : ONE;
    }

    public void trigger(ServerPlayerEntity player) {
        trigger(player, conditions -> conditions.test(player));
    }

    public static class TotalCountConditions extends KillTrainerCriterion.Conditions {
        private final int count;

        public TotalCountConditions(int count) {
            super(ID, LootContextPredicate.EMPTY);
            this.count = count;
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("count", count);

            return jsonObject;
        }

        boolean test(ServerPlayerEntity player) {
            int total = PlayerHistoryManager.getPlayerHistory(player.getUuid()).getTotalTrainerKillCount();
            return total >= count;
        }
    }

    public static class TrainerCountConditions extends KillTrainerCriterion.Conditions {
        private final Identifier trainer;
        private final int count;

        public TrainerCountConditions(Identifier trainer) {
            this(trainer, ONE);
        }

        public TrainerCountConditions(Identifier trainer, int count) {
            super(ID, LootContextPredicate.EMPTY);
            this.trainer = trainer;
            this.count = count;
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("trainer", trainer.toString());
            jsonObject.addProperty("count", count);

            return jsonObject;
        }

        boolean test(ServerPlayerEntity player) {
            int record = ((EntityRecord) PlayerHistoryManager.getPlayerHistory(player.getUuid()).getOrCreateRecord(trainer)).getKillCount();
            return record >= count;
        }
    }

    public static abstract class Conditions extends AbstractCriterionConditions {
        public Conditions(Identifier id, LootContextPredicate entity) {
            super(id, entity);
        }

        abstract boolean test(ServerPlayerEntity player);
    }
}
