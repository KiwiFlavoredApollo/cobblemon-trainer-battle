package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrainerBattleRegistry {
    private static Map<UUID, TrainerBattle> battle = new HashMap<>();

    public static void put(UUID uuid, TrainerBattle trainerBattle) {
        TrainerBattleRegistry.battle.put(uuid, trainerBattle);
    }

    public static TrainerBattle get(UUID uuid) {
        return TrainerBattleRegistry.battle.get(uuid);
    }

    public static boolean containsKey(UUID uuid) {
        return TrainerBattleRegistry.battle.containsKey(uuid);
    }

    public static void remove(UUID uuid) {
        TrainerBattleRegistry.battle.remove(uuid);
    }

    public static Collection<TrainerBattle> values() {
        return TrainerBattleRegistry.battle.values();
    }

    public static void removeDisconnectedPlayerBattle(ServerPlayNetworkHandler handler, MinecraftServer server) {
        ServerPlayerEntity player = handler.getPlayer();

        if (!TrainerBattleRegistry.containsKey(player.getUuid())) {
            return;
        }

        TrainerBattleRegistry.get(player.getUuid()).onPlayerDefeat();
        TrainerBattleRegistry.remove(player.getUuid());
    }
}
