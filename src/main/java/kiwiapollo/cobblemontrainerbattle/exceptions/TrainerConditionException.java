package kiwiapollo.cobblemontrainerbattle.exceptions;

import kiwiapollo.cobblemontrainerbattle.common.TrainerConditionKey;

public class TrainerConditionException extends Exception {
    private TrainerConditionKey conditionKey;
    private Object conditionValue;

    public TrainerConditionException(String message) {
        super(message);
        conditionKey = null;
        conditionValue = null;
    }

    public TrainerConditionException(String message, TrainerConditionKey conditionKey, Object conditionValue) {
        this(message);
        this.conditionKey = conditionKey;
        this.conditionValue = conditionValue;
    }

    public TrainerConditionKey getConditionKey() {
        return conditionKey;
    }

    public Object getConditionValue() {
        return conditionValue;
    }
}
