package kiwiapollo.cobblemontrainerbattle.exceptions;

import kiwiapollo.cobblemontrainerbattle.common.InvalidResourceState;

public class InvalidResourceStateException extends Exception {
    private final InvalidResourceState invalidResourceState;
    private final String resourcePath;

    public InvalidResourceStateException(String message) {
        super(message);
        invalidResourceState = null;
        resourcePath = null;
    }

    public InvalidResourceStateException(InvalidResourceState invalidResourceState, String resourcePath, String message) {
        super(message);
        this.invalidResourceState = invalidResourceState;
        this.resourcePath = resourcePath;
    }

    public InvalidResourceState getInvalidResourceState() {
        return invalidResourceState;
    }

    public String getResourcePath() {
        return resourcePath;
    }
}
