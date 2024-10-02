package kiwiapollo.cobblemontrainerbattle.battleactors.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import kiwiapollo.cobblemontrainerbattle.battlefrontier.BattleFrontier;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;

public class BattleFrontierNameTrainerBattleActorFactory {
    public BattleActor create(Trainer trainer) {
        return new FlatLevelFullHealthTrainerBattleActorFactory().create(trainer, BattleFrontier.LEVEL);
    }
}
