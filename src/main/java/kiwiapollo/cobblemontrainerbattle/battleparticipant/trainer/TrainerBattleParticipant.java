package kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer;

import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.BattleParticipant;
import kiwiapollo.cobblemontrainerbattle.common.BattleCondition;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public interface TrainerBattleParticipant extends BattleParticipant {
    Identifier getIdentifier();

    BattleAI getBattleAI();

    BattleCondition getBattleCondition();

    SoundEvent getBattleTheme();
}
