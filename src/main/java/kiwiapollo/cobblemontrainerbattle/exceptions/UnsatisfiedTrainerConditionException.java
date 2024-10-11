package kiwiapollo.cobblemontrainerbattle.exceptions;

import kiwiapollo.cobblemontrainerbattle.common.TrainerConditionType;

public class UnsatisfiedTrainerConditionException extends Exception {
    private final TrainerConditionType trainerConditionType;
    private final Object requiredValue;

    public UnsatisfiedTrainerConditionException(TrainerConditionType trainerConditionType, Object requiredValue) {
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
