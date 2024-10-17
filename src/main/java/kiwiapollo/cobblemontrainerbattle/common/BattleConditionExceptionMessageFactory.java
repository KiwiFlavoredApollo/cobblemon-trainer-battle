package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.exception.BattleConditionException;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class BattleConditionExceptionMessageFactory {
    public MutableText create(BattleConditionException e) {
        return switch (e.getBattleConditionType()) {
            case MAXIMUM_PARTY_LEVEL -> Text.translatable("command.cobblemontrainerbattle.condition.maximum_party_level", e.getRequiredValue());
            case MINIMUM_PARTY_LEVEL -> Text.translatable("command.cobblemontrainerbattle.condition.minimum_party_level", e.getRequiredValue());
        };
    }
}
