package kiwiapollo.cobblemontrainerbattle.battleparticipant;

import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import kiwiapollo.cobblemontrainerbattle.common.BattleCondition;

public interface TrainerBattleParticipant extends BattleParticipant {
    BattleAI getBattleAI();

    BattleCondition getBattleCondition();
}
