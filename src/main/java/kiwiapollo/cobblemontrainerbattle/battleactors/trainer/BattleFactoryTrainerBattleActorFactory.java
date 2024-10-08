package kiwiapollo.cobblemontrainerbattle.battleactors.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import kiwiapollo.cobblemontrainerbattle.battlefactory.BattleFactory;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;
import net.minecraft.server.network.ServerPlayerEntity;

public class BattleFactoryTrainerBattleActorFactory {
    public BattleActor create(Trainer trainer, ServerPlayerEntity player) {
        return new FlatLevelFullHealthTrainerBattleActorFactory().create(trainer, player, BattleFactory.LEVEL);
    }
}
