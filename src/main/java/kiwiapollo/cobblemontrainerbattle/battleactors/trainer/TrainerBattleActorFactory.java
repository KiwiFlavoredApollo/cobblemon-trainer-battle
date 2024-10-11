package kiwiapollo.cobblemontrainerbattle.battleactors.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import net.minecraft.util.Identifier;

public interface TrainerBattleActorFactory {
    BattleActor createWithStatusQuo(Identifier identifier);
    BattleActor createWithFlatLevelFullHealth(Identifier identifier, int level);
}
