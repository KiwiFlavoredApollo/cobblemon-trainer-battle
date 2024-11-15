package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer;

import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.BattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.MessagePredicate;
import net.minecraft.util.Identifier;

import java.util.List;

public interface TrainerBattleParticipant extends BattleParticipant {
    Identifier getIdentifier();

    BattleAI getBattleAI();

    List<MessagePredicate<PlayerBattleParticipant>> getPredicates();
}
