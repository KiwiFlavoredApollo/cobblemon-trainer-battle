package kiwiapollo.cobblemontrainerbattle.groupbattle;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public interface GroupBattleSessionFactory {
    GroupBattleSession create(ServerPlayerEntity player, Identifier group);
}
