package kiwiapollo.cobblemontrainerbattle.exceptions;

public class TrainerConditionNotSatisfiedException extends Exception {
    public final String message;

    public TrainerConditionNotSatisfiedException(String message) {
        this.message = message;
    }
}
