package kiwiapollo.cobblemontrainerbattle.battleactors.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import kiwiapollo.cobblemontrainerbattle.battlefactory.BattleFactory;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;

public class BattleFactoryNameTrainerBattleActorFactory {
    public BattleActor create(Trainer trainer) {
        return new FlatLevelFullHealthTrainerBattleActorFactory().create(trainer, BattleFactory.LEVEL);
    }
}
