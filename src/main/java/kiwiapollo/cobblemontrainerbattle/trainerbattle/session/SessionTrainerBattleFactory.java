package kiwiapollo.cobblemontrainerbattle.trainerbattle.session;

import kiwiapollo.cobblemontrainerbattle.session.Session;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public interface SessionTrainerBattleFactory {
    TrainerBattle create(ServerPlayerEntity player, Identifier trainer, Session session);
}
