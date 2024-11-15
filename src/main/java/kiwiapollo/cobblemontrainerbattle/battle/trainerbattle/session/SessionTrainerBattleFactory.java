package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.session;

import kiwiapollo.cobblemontrainerbattle.battle.session.Session;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public interface SessionTrainerBattleFactory {
    TrainerBattle create(ServerPlayerEntity player, Identifier trainer, Session session);
}
