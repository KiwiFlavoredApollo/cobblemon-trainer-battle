package kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer;

import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.BattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.common.BattleCondition;
import kiwiapollo.cobblemontrainerbattle.predicates.MessagePredicate;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.List;

public interface TrainerBattleParticipant extends BattleParticipant {
    Identifier getIdentifier();

    BattleAI getBattleAI();

    SoundEvent getBattleTheme();

    List<MessagePredicate<PlayerBattleParticipant>> getPredicates();
}
