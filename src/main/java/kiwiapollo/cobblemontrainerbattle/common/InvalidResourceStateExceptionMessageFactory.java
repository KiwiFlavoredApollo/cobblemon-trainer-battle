package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.exceptions.InvalidResourceStateException;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class InvalidResourceStateExceptionMessageFactory {
    public MutableText create(InvalidResourceStateException e) {
        return switch (e.getReason()) {
            case NOT_FOUND ->
                    Text.translatable("command.cobblemontrainerbattle.common.resource.not_found", e.getResourcePath());
            case CANNOT_BE_READ ->
                    Text.translatable("command.cobblemontrainerbattle.common.resource.cannot_be_read", e.getResourcePath());
        };
    }
}
