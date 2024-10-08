package kiwiapollo.cobblemontrainerbattle.battleactors.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;

public interface TrainerBattleActorFactory {
    BattleActor createWithStatusQuo(Trainer trainer);
    BattleActor createWithFlatLevelFullHealth(Trainer trainer, int level);
}
