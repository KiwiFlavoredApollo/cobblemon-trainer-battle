package kiwiapollo.cobblemontrainerbattle.exceptions;

import kiwiapollo.cobblemontrainerbattle.common.InvalidPlayerState;

public class InvalidPlayerStateException extends Exception {
    private final InvalidPlayerState invalidPlayerState;

    public InvalidPlayerStateException(String message) {
        super(message);
        invalidPlayerState = null;
    }

    public InvalidPlayerStateException(String message, InvalidPlayerState invalidPlayerState) {
        super(message);
        this.invalidPlayerState = invalidPlayerState;
    }

    public InvalidPlayerState getInvalidPlayerState() {
        return invalidPlayerState;
    }
}
