package kiwiapollo.cobblemontrainerbattle.history;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.WorldSavePath;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerHistoryStorage implements ServerLifecycleEvents.ServerStarted, ServerLifecycleEvents.ServerStopped, ServerTickEvents.EndTick {
    private static final int SAVE_INTERVAL_IN_MINUTES = 20;

    private static PlayerHistoryStorage instance;
    private static Map<UUID, PlayerHistory> storage;

    private PlayerHistoryStorage() {
        storage = new HashMap<>();
    }

    public static PlayerHistoryStorage getInstance() {
        if (instance == null) {
            instance = new PlayerHistoryStorage();
        }

        return instance;
    }

    public PlayerHistory get(ServerPlayerEntity player) {
        if (!storage.containsKey(player.getUuid())) {
            storage.put(player.getUuid(), new PlayerHistory());
        }

        return storage.get(player.getUuid());
    }

    @Override
    public void onServerStarted(MinecraftServer server) {
        load(server);
    }

    @Override
    public void onServerStopped(MinecraftServer server) {
        save(server);
    }

    @Override
    public void onEndTick(MinecraftServer server) {
        if (server.getTicks() % getSaveIntervalInTicks() == 0) {
            save(server);
        }
    }

    private boolean isDatFile(File file) {
        return file.getName().endsWith(".dat");
    }

    private int getSaveIntervalInTicks() {
        return SAVE_INTERVAL_IN_MINUTES * SharedConstants.TICKS_PER_MINUTE;
    }

    // TODO: is this right place
    public File getHistoryPath(MinecraftServer server) {
        final String historyDir = "history";

        File worldDir = server.getSavePath(WorldSavePath.ROOT).toFile();
        String historyPath = CobblemonTrainerBattle.MOD_ID + "/" + historyDir;

        return new File(worldDir, historyPath);
    }

    private void load(MinecraftServer server) {
        File historyPath = getHistoryPath(server);

        if (!historyPath.isDirectory()) {
            return;
        }

        storage.clear();

        List<File> datFileList = Arrays.stream(historyPath.listFiles()).filter(this::isDatFile).toList();

        for (File file : datFileList) {
            try {
                UUID uuid = UUID.fromString(file.getName().replace(".dat", ""));

                PlayerHistory history = new PlayerHistory();
                history.readFromNbt(NbtIo.readCompressed(file));

                storage.put(uuid, history);

            } catch (NullPointerException | IOException e) {
                UUID uuid = UUID.fromString(file.getName().replace(".dat", ""));

                File backup = new File(historyPath, String.format("%s.dat_bak", uuid));
                file.renameTo(backup);
                PlayerHistory history = new PlayerHistory();

                storage.put(uuid, history);

                CobblemonTrainerBattle.LOGGER.error("Error occurred while loading {}", file.getName());
                CobblemonTrainerBattle.LOGGER.error("Created backup : {}", backup.getName());
            }
        }

        CobblemonTrainerBattle.LOGGER.info("Loaded player histories");
    }

    private void save(MinecraftServer server) {
        File historyPath = getHistoryPath(server);

        if (!historyPath.exists()) {
            historyPath.mkdirs();
        }

        for (Map.Entry<UUID, PlayerHistory> entry: storage.entrySet()) {
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
}
