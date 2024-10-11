package kiwiapollo.cobblemontrainerbattle.exceptions;

import kiwiapollo.cobblemontrainerbattle.common.BattleConditionType;

public class BattleConditionException extends Exception {
    private final BattleConditionType battleConditionType;
    private final Object requiredValue;

    public BattleConditionException(BattleConditionType battleConditionType, Object requiredValue) {
        this.battleConditionType = battleConditionType;
        this.requiredValue = requiredValue;
    }

    public BattleConditionType getBattleConditionType() {
        return battleConditionType;
    }

    public Object getRequiredValue() {
        return requiredValue;
    }
}
