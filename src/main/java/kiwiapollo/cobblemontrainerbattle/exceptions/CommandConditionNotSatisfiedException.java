package kiwiapollo.cobblemontrainerbattle.exceptions;

import kiwiapollo.cobblemontrainerbattle.common.CommandConditionType;

public class CommandConditionNotSatisfiedException extends Exception {
    private final CommandConditionType commandConditionType;

    public CommandConditionNotSatisfiedException(String message) {
        super(message);
        commandConditionType = null;
    }

    public CommandConditionNotSatisfiedException(String message, CommandConditionType commandConditionType) {
        super(message);
        this.commandConditionType = commandConditionType;
    }

    public CommandConditionType getCommandConditionType() {
        return commandConditionType;
    }
}
