package kiwiapollo.cobblemontrainerbattle.global.history;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class PlayerHistorySaver implements ServerLifecycleEvents.ServerStopped, ServerTickEvents.EndTick {
    private static final int SAVE_INTERVAL = 24000;

    private void save(MinecraftServer server) {
        File historyPath = PlayerHistoryUtil.getHistoryPath(server);

        if (!historyPath.exists()) {
            historyPath.mkdirs();
        }

        for (Map.Entry<UUID, PlayerHistory> entry: PlayerHistoryStorage.getInstance().entrySet()) {
            try {
                UUID uuid = entry.getKey();
                PlayerHistory history = entry.getValue();

                File newHistory = new File(historyPath, String.format("%s.dat", uuid));
                File oldHistory = new File(historyPath, String.format("%s.dat_old", uuid));

                if (newHistory.exists()) {
                    newHistory.renameTo(oldHistory);
                }

                NbtIo.writeCompressed(history.writeToNbt(new NbtCompound()), newHistory);

            } catch (IOException e) {
                UUID uuid = entry.getKey();
                File file = new File(historyPath, String.format("%s.dat", uuid));
                CobblemonTrainerBattle.LOGGER.error("Error occurred while saving {}", file.getName());
            }
        }

        CobblemonTrainerBattle.LOGGER.info("Saved player histories");
    }

    @Override
    public void onServerStopped(MinecraftServer server) {
        save(server);
    }

    @Override
    public void onEndTick(MinecraftServer server) {
        if (server.getTicks() % SAVE_INTERVAL == 0) {
            save(server);
        }
    }
}
