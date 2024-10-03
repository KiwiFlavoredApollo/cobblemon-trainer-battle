package kiwiapollo.cobblemontrainerbattle.exceptions;

import kiwiapollo.cobblemontrainerbattle.common.InvalidBattleSessionState;

public class InvalidBattleSessionStateException extends Exception {
    private final InvalidBattleSessionState invalidBattleSessionState;

    public InvalidBattleSessionStateException(String message) {
        super(message);
        invalidBattleSessionState = null;
    }

    public InvalidBattleSessionStateException(String message, InvalidBattleSessionState invalidBattleSessionState) {
        super(message);
        this.invalidBattleSessionState = invalidBattleSessionState;
    }

    public InvalidBattleSessionState getInvalidBattleSessionState() {
        return invalidBattleSessionState;
    }
}
