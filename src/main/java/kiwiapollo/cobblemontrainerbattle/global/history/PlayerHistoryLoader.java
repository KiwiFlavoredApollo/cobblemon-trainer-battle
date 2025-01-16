package kiwiapollo.cobblemontrainerbattle.global.history;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PlayerHistoryLoader implements ServerLifecycleEvents.ServerStarted {
    @Override
    public void onServerStarted(MinecraftServer server) {
        File worldDir = server.getSavePath(WorldSavePath.ROOT).toFile();
        File historyDir = new File(worldDir, CobblemonTrainerBattle.MOD_ID);

        if (!historyDir.isDirectory()) {
            return;
        }

        PlayerHistoryStorage.getInstance().clear();
        List<File> datFileList = Arrays.stream(historyDir.listFiles()).filter(this::isDatFile).toList();
        for (File file : datFileList) {
            try {
                UUID uuid = UUID.fromString(file.getName().replace(".dat", ""));

                PlayerHistory history = new PlayerHistory();
                history.readFromNbt(NbtIo.readCompressed(file));

                PlayerHistoryStorage.getInstance().put(uuid, history);

            } catch (NullPointerException | IOException e) {
                UUID uuid = UUID.fromString(file.getName().replace(".dat", ""));

                File backup = new File(historyDir, String.format("%s.dat_bak", uuid));
                file.renameTo(backup);
                PlayerHistory history = new PlayerHistory();

                PlayerHistoryStorage.getInstance().put(uuid, history);

                CobblemonTrainerBattle.LOGGER.error("Error occurred while loading {}", file.getName());
                CobblemonTrainerBattle.LOGGER.error("Created backup : {}", backup.getName());
            }
        }

        CobblemonTrainerBattle.LOGGER.info("Loaded player histories");
    }

    private boolean isDatFile(File file) {
        return file.getName().endsWith(".dat");
    }
}
