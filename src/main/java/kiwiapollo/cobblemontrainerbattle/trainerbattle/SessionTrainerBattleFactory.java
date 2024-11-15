package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.session.Session;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public interface SessionTrainerBattleFactory {
    TrainerBattle create(ServerPlayerEntity player, Identifier trainer, Session session);
}
