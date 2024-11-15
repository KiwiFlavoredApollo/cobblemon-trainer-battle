package kiwiapollo.cobblemontrainerbattle.battle.session;

import kiwiapollo.cobblemontrainerbattle.battle.battlefactory.BattleFactorySession;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class BattleFactorySessionStorage {
    private static final SessionRegistry<BattleFactorySession> registry = new SessionRegistry<>();

    public static SessionRegistry<BattleFactorySession> getSessionRegistry() {
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
