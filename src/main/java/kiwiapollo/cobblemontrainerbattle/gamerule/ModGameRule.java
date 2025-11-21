package kiwiapollo.cobblemontrainerbattle.gamerule;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class ModGameRule {
    public static GameRules.Key<GameRules.IntRule> MAXIMUM_TRAINER_SPAWN_COUNT;
    public static GameRules.Key<GameRules.IntRule> TRAINER_SPAWN_INTERVAL_IN_SECONDS;

    public static void register() {
        MAXIMUM_TRAINER_SPAWN_COUNT = GameRuleRegistry.register("maximumTrainerSpawnCount", GameRules.Category.MISC, GameRuleFactory.createIntRule(1, 0, Integer.MAX_VALUE));
        TRAINER_SPAWN_INTERVAL_IN_SECONDS = GameRuleRegistry.register("trainerSpawnIntervalInSeconds", GameRules.Category.MISC, GameRuleFactory.createIntRule(60, 1, Integer.MAX_VALUE));
    }
}
