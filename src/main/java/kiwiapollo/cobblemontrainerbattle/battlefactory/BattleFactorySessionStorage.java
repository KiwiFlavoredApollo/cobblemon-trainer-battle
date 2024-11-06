package kiwiapollo.cobblemontrainerbattle.battlefactory;

import kiwiapollo.cobblemontrainerbattle.session.Session;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BattleFactorySessionStorage {
    private static Map<UUID, BattleFactorySession> sessions = new HashMap<>();

    public static void removeDisconnectedPlayerSession(ServerPlayNetworkHandler handler, MinecraftServer server) {
        ServerPlayerEntity player = handler.getPlayer();

        if (!sessions.containsKey(player.getUuid())) {
            return;
        }

        sessions.get(player.getUuid()).onSessionStop();
        sessions.remove(player.getUuid());
    }

    public static boolean containsKey(UUID uuid) {
        return sessions.containsKey(uuid);
    }

    public static void remove(UUID uuid) {
        sessions.remove(uuid);
    }

    public static BattleFactorySession get(UUID uuid) {
        return sessions.get(uuid);
    }

    public static void put(UUID uuid, BattleFactorySession session) {
        sessions.put(uuid, session);
    }

    public static void assertSessionExist(ServerPlayerEntity player) {
        if (!isSessionExist(player)) {
            throw new IllegalStateException();
        }
    }

    public static void assertSessionNotExist(ServerPlayerEntity player) {
        if (isSessionExist(player)) {
            throw new IllegalStateException();
        }
    }

    private static boolean isSessionExist(ServerPlayerEntity player) {
        return sessions.containsKey(player.getUuid());
    }
}
