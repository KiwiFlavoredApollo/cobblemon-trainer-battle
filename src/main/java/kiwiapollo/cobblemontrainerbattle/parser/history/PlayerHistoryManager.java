package kiwiapollo.cobblemontrainerbattle.parser.history;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.WorldSavePath;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerHistoryManager {
    private static final int SAVE_INTERVAL = 24000;

    private static final Map<UUID, PlayerHistory> histories = new HashMap<>();

    public static boolean containsKey(UUID uuid) {
        return PlayerHistoryManager.histories.containsKey(uuid);
    }

    public static PlayerHistory getPlayerHistory(UUID uuid) {
        return PlayerHistoryManager.histories.get(uuid);
    }

    public static void initializePlayerHistoryIfNotExist(
            ServerPlayNetworkHandler handler,
            PacketSender sender,
            MinecraftServer server
    ) {
        UUID uuid = handler.getPlayer().getUuid();

        if (PlayerHistoryManager.containsKey(uuid)) {
            return;
        }

        PlayerHistoryManager.histories.put(uuid, new PlayerHistory());
    }

    public static void loadPlayerHistory(MinecraftServer server) {
        File historyDir = getTrainerBattleHistoryDir(server);

        if (!historyDir.isDirectory()) {
            return;
        }

        PlayerHistoryManager.histories.clear();
        List<File> datFileList = Arrays.stream(historyDir.listFiles()).filter(PlayerHistoryManager::isDatFile).toList();
        for (File file : datFileList) {
            try {
                UUID uuid = UUID.fromString(file.getName().replace(".dat", ""));
                PlayerHistory history = new PlayerHistory();
                history.readFromNbt(NbtIo.readCompressed(file));
                PlayerHistoryManager.histories.put(uuid, history);

            } catch (NullPointerException | IOException e) {
                UUID uuid = UUID.fromString(file.getName().replace(".dat", ""));

                File backup = new File(historyDir, String.format("%s.dat_bak", uuid));
                file.renameTo(backup);

                PlayerHistoryManager.histories.put(uuid, new PlayerHistory());

                CobblemonTrainerBattle.LOGGER.error("Error occurred while loading {}", file.getName());
                CobblemonTrainerBattle.LOGGER.error("Created backup : {}", backup.getName());
            }
        }

        CobblemonTrainerBattle.LOGGER.info("Loaded player histories");
    }

    public static void savePlayerHistory(MinecraftServer server) {
        File historyDir = getTrainerBattleHistoryDir(server);

        if (!historyDir.exists()) {
            historyDir.mkdirs();
        }

        for (Map.Entry<UUID, PlayerHistory> entry: PlayerHistoryManager.histories.entrySet()) {
            try {
                UUID uuid = entry.getKey();
                PlayerHistory history = entry.getValue();

                File newHistory = new File(historyDir, String.format("%s.dat", uuid));
                File oldHistory = new File(historyDir, String.format("%s.dat_old", uuid));

                if (newHistory.exists()) {
                    newHistory.renameTo(oldHistory);
                }

                NbtCompound nbt = new NbtCompound();
                history.writeToNbt(nbt);
                NbtIo.writeCompressed(nbt, newHistory);

            } catch (IOException e) {
                UUID uuid = entry.getKey();
                File file = new File(historyDir, String.format("%s.dat", uuid));
                CobblemonTrainerBattle.LOGGER.error("Error occurred while saving {}", file.getName());
            }
        }

        CobblemonTrainerBattle.LOGGER.info("Saved player histories");
    }

    public static void periodicallySavePlayerHistory(MinecraftServer server) {
        if (server.getTicks() % SAVE_INTERVAL == 0) {
            PlayerHistoryManager.savePlayerHistory(server);
        }
    }

    private static File getTrainerBattleHistoryDir(MinecraftServer server) {
        File worldDir = server.getSavePath(WorldSavePath.ROOT).toFile();

        return new File(worldDir, CobblemonTrainerBattle.MOD_ID);
    }

    private static boolean isDatFile(File file) {
        return file.getName().endsWith(".dat");
    }
}
