package kiwiapollo.cobblemontrainerbattle.exceptions;

import kiwiapollo.cobblemontrainerbattle.common.InvalidPlayerStateType;

public class InvalidPlayerStateException extends Exception {
    private final InvalidPlayerStateType invalidPlayerStateType;

    public InvalidPlayerStateException(String message) {
        super(message);
        invalidPlayerStateType = null;
    }

    public InvalidPlayerStateException(String message, InvalidPlayerStateType invalidPlayerStateType) {
        super(message);
        this.invalidPlayerStateType = invalidPlayerStateType;
    }

    public InvalidPlayerStateType getInvalidPlayerStateType() {
        return invalidPlayerStateType;
    }
}
