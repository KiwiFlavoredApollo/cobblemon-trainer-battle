package kiwiapollo.cobblemontrainerbattle.exceptions;

import kiwiapollo.cobblemontrainerbattle.common.TrainerCondition;

public class UnsatisfiedTrainerConditionException extends Exception {
    private final TrainerCondition unsatisfiedCondition;
    private final Object requiredValue;

    public UnsatisfiedTrainerConditionException(String message) {
        super(message);
        unsatisfiedCondition = null;
        requiredValue = null;
    }

    public UnsatisfiedTrainerConditionException(String message, TrainerCondition unsatisfiedCondition, Object requiredValue) {
        super(message);
        this.unsatisfiedCondition = unsatisfiedCondition;
        this.requiredValue = requiredValue;
    }

    public TrainerCondition getUnsatisfiedCondition() {
        return unsatisfiedCondition;
    }

    public Object getRequiredValue() {
        return requiredValue;
    }
}
