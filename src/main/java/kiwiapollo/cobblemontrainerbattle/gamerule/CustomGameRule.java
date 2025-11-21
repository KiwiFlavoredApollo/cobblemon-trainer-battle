package kiwiapollo.cobblemontrainerbattle.gamerule;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class CustomGameRule {
    public static GameRules.Key<GameRules.IntRule> MAXIMUM_PER_PLAYER_TRAINER_COUNT;
    public static GameRules.Key<GameRules.IntRule> TRAINER_SPAWN_INTERVAL_IN_SECONDS;
    public static GameRules.Key<GameRules.BooleanRule> DO_TRAINER_APPLY_STATUS_CONDITION;

    public static void register() {
        MAXIMUM_PER_PLAYER_TRAINER_COUNT = GameRuleRegistry.register("maximumPerPlayerTrainerCount", GameRules.Category.MISC, GameRuleFactory.createIntRule(1, 0, Integer.MAX_VALUE));
        TRAINER_SPAWN_INTERVAL_IN_SECONDS = GameRuleRegistry.register("trainerSpawnIntervalInSeconds", GameRules.Category.MISC, GameRuleFactory.createIntRule(60, 1, Integer.MAX_VALUE));
        DO_TRAINER_APPLY_STATUS_CONDITION = GameRuleRegistry.register("doTrainerApplyStatusCondition", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));
    }
}
