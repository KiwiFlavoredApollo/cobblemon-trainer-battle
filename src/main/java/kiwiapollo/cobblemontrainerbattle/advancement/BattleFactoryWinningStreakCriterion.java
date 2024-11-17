package kiwiapollo.cobblemontrainerbattle.advancement;

import com.google.gson.JsonObject;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.parser.history.PlayerHistoryManager;
import kiwiapollo.cobblemontrainerbattle.parser.history.MaximumStreakRecord;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class BattleFactoryWinningStreakCriterion extends AbstractCriterion<BattleFactoryWinningStreakCriterion.Condition> {
    private static final Identifier ID = Identifier.of(CobblemonTrainerBattle.MOD_ID, "battlefactory_winning_streak");

    @Override
    protected Condition conditionsFromJson(
            JsonObject obj,
            LootContextPredicate playerPredicate,
            AdvancementEntityPredicateDeserializer predicateDeserializer
    ) {
        int streak = obj.get("count").getAsInt();
        return new BattleFactoryWinningStreakCriterion.Condition(streak);
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player) {
        trigger(player, conditions -> conditions.test(player));
    }

    public static class Condition extends AbstractCriterionConditions {
        private final int count;

        public Condition(int count) {
            super(ID, LootContextPredicate.EMPTY);
            this.count = count;
        }

        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("count", count);
            return jsonObject;
        }

        public boolean test(ServerPlayerEntity player) {
            MaximumStreakRecord record = (MaximumStreakRecord) PlayerHistoryManager.get(player.getUuid()).get(Identifier.tryParse("minigame:battlefactory"));
            return record.getMaximumStreak() >= count;
        }
    }
}
