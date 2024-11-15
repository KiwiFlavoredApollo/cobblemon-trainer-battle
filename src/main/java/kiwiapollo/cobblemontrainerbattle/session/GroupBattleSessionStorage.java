package kiwiapollo.cobblemontrainerbattle.session;

import kiwiapollo.cobblemontrainerbattle.groupbattle.GroupBattleSession;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class GroupBattleSessionStorage {
    private static final SessionRegistry<GroupBattleSession> registry = new SessionRegistry<GroupBattleSession>();

    public static SessionRegistry<GroupBattleSession> getSessionRegistry() {
        return registry;
    }

    public static void removeDisconnectedPlayerSession(ServerPlayNetworkHandler handler, MinecraftServer server) {
        ServerPlayerEntity player = handler.getPlayer();

        if (!registry.containsKey(player.getUuid())) {
            return;
        }

        registry.get(player.getUuid()).onSessionStop();
        registry.remove(player.getUuid());
    }
}
