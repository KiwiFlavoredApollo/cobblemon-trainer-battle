package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrainerBattleStorage {
    private static Map<UUID, TrainerBattle> battles = new HashMap<>();

    public static void put(UUID uuid, TrainerBattle trainerBattle) {
        TrainerBattleStorage.battles.put(uuid, trainerBattle);
    }

    public static TrainerBattle get(UUID uuid) {
        return TrainerBattleStorage.battles.get(uuid);
    }

    public static boolean containsKey(UUID uuid) {
        return TrainerBattleStorage.battles.containsKey(uuid);
    }

    public static void remove(UUID uuid) {
        TrainerBattleStorage.battles.remove(uuid);
    }

    public static Collection<TrainerBattle> values() {
        return TrainerBattleStorage.battles.values();
    }

    public static void removeDisconnectedPlayerBattle(ServerPlayNetworkHandler handler, MinecraftServer server) {
        ServerPlayerEntity player = handler.getPlayer();

        if (!battles.containsKey(player.getUuid())) {
            return;
        }

        battles.get(player.getUuid()).onPlayerDefeat();
        battles.remove(player.getUuid());
    }
}
