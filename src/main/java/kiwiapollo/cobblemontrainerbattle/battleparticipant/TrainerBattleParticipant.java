package kiwiapollo.cobblemontrainerbattle.battleparticipant;

import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor;
import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import kiwiapollo.cobblemontrainerbattle.common.BattleCondition;
import net.minecraft.util.Identifier;

public interface TrainerBattleParticipant extends BattleParticipant {
    Identifier getIdentifier();

    BattleAI getBattleAI();

    BattleCondition getBattleCondition();
}
