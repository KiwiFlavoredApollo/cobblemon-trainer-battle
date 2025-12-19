package kiwiapollo.cobblemontrainerbattle.advancement;

import net.minecraft.advancement.criterion.Criteria;

public class CustomCriteria {
    public static final DefeatTrainerCriterion DEFEAT_TRAINER_CRITERION = new DefeatTrainerCriterion();
    public static final KillTrainerCriterion KILL_TRAINER_CRITERION = new KillTrainerCriterion();

    public static void initialize() {
        Criteria.register(CustomCriteria.DEFEAT_TRAINER_CRITERION);
        Criteria.register(CustomCriteria.KILL_TRAINER_CRITERION);
    }
}
