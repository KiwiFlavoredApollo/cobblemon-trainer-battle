package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class TrainerBattleStorage {
    private static final TrainerBattleRegistry storage = new TrainerBattleRegistry();

    public static TrainerBattleRegistry getTrainerBattleRegistry() {
        return storage;
    }

    public static void removeDisconnectedPlayerBattle(ServerPlayNetworkHandler handler, MinecraftServer server) {
        ServerPlayerEntity player = handler.getPlayer();

        if (!storage.containsKey(player.getUuid())) {
            return;
        }

        storage.get(player.getUuid()).onPlayerDefeat();
        storage.remove(player.getUuid());
    }
}
