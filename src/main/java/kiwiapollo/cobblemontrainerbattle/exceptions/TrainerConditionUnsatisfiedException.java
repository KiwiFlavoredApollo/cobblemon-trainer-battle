package kiwiapollo.cobblemontrainerbattle.exceptions;

import kiwiapollo.cobblemontrainerbattle.common.TrainerConditionType;

public class TrainerConditionUnsatisfiedException extends Exception {
    private final TrainerConditionType trainerConditionType;
    private final Object requiredValue;

    public TrainerConditionUnsatisfiedException(String message) {
        super(message);
        trainerConditionType = null;
        requiredValue = null;
    }

    public TrainerConditionUnsatisfiedException(String message, TrainerConditionType trainerConditionType, Object requiredValue) {
        super(message);
        this.trainerConditionType = trainerConditionType;
        this.requiredValue = requiredValue;
    }

    public TrainerConditionType getTrainerConditionType() {
        return trainerConditionType;
    }

    public Object getRequiredValue() {
        return requiredValue;
    }
}
