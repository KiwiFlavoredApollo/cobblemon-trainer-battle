package kiwiapollo.cobblemontrainerbattle.global.context;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class BattleContextRemover implements ServerPlayConnectionEvents.Disconnect {
    @Override
    public void onPlayDisconnect(ServerPlayNetworkHandler handler, MinecraftServer server) {
        ServerPlayerEntity player = handler.getPlayer();

        BattleContextStorage.getInstance().getOrCreate(player.getUuid()).getTrainerBattle().onPlayerDefeat();
        BattleContextStorage.getInstance().remove(player.getUuid());
    }
}
