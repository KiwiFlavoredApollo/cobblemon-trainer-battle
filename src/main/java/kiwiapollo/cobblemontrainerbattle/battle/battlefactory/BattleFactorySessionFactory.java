package kiwiapollo.cobblemontrainerbattle.battle.battlefactory;

import net.minecraft.server.network.ServerPlayerEntity;

public interface BattleFactorySessionFactory {
    BattleFactorySession create(ServerPlayerEntity player);
}
