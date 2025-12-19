package kiwiapollo.cobblemontrainerbattle.gamerule;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class CustomGameRule {
    public static GameRules.Key<GameRules.IntRule> MAXIMUM_TRAINER_COUNT_PER_PLAYER = GameRuleRegistry.register("maximumTrainerCountPerPlayer", GameRules.Category.MISC, GameRuleFactory.createIntRule(1, 0, Integer.MAX_VALUE));
    public static GameRules.Key<GameRules.IntRule> TRAINER_SPAWN_INTERVAL_IN_SECONDS = GameRuleRegistry.register("trainerSpawnIntervalInSeconds", GameRules.Category.MISC, GameRuleFactory.createIntRule(60, 1, Integer.MAX_VALUE));
    public static GameRules.Key<GameRules.IntRule> TRAINER_FLEE_DISTANCE_IN_BLOCKS = GameRuleRegistry.register("trainerFleeDistanceInBlocks", GameRules.Category.MISC, GameRuleFactory.createIntRule(32, 16, 128));
    public static GameRules.Key<GameRules.BooleanRule> DO_TRAINER_APPLY_STATUS_CONDITION = GameRuleRegistry.register("doTrainerApplyStatusCondition", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));

    public static void initialize() {

    }
}
