package kiwiapollo.cobblemontrainerbattle.exceptions;

import kiwiapollo.cobblemontrainerbattle.common.InvalidResourceState;

public class InvalidResourceStateException extends Exception {
    private final InvalidResourceState reason;
    private final String resourcePath;

    public InvalidResourceStateException(String message) {
        super(message);
        reason = null;
        resourcePath = null;
    }

    public InvalidResourceStateException(String message, InvalidResourceState reason, String resourcePath) {
        super(message);
        this.reason = reason;
        this.resourcePath = resourcePath;
    }

    public InvalidResourceState getReason() {
        return reason;
    }

    public String getResourcePath() {
        return resourcePath;
    }
}
