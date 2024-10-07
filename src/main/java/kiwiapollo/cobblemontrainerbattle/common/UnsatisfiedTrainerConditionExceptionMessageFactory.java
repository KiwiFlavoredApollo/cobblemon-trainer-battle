package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.exceptions.UnsatisfiedTrainerConditionException;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class UnsatisfiedTrainerConditionExceptionMessageFactory {
    public MutableText create(UnsatisfiedTrainerConditionException e) {
        return switch (e.getTrainerConditionType()) {
            case MAXIMUM_PARTY_LEVEL -> Text.translatable("command.cobblemontrainerbattle.trainer.maximum_party_level", e.getRequiredValue());
            case MINIMUM_PARTY_LEVEL -> Text.translatable("command.cobblemontrainerbattle.trainer.minimum_party_level", e.getRequiredValue());
        };
    }
}
