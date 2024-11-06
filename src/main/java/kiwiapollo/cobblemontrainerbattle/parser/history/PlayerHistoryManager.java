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

    private static Map<UUID, PlayerHistory> histories = new HashMap<>();

    public static boolean containsKey(UUID uuid) {
        return PlayerHistoryManager.histories.containsKey(uuid);
    }

    public static PlayerHistory get(UUID uuid) {
        return PlayerHistoryManager.histories.get(uuid);
    }

    public static void put(UUID uuid, PlayerHistory history) {
        PlayerHistoryManager.histories.put(uuid, history);
    }

    public static void initializePlayerHistory(
            ServerPlayNetworkHandler handler,
            PacketSender sender,
            MinecraftServer server
    ) {
        UUID playerUuid = handler.getPlayer().getUuid();

        if (PlayerHistoryManager.containsKey(playerUuid)) {
            return;
        }

        PlayerHistoryManager.put(playerUuid, new PlayerHistory());
    }

    public static void periodicallySavePlayerHistory(MinecraftServer server) {
        if (server.getTicks() % SAVE_INTERVAL == 0) {
            PlayerHistoryManager.saveToNbt(server);
        }
    }

    public static void loadFromNbt(MinecraftServer server) {
        File historyDir = getTrainerBattleHistoryDir(server);

        if (!historyDir.isDirectory()) {
            return;
        }

        PlayerHistoryManager.histories.clear();
        List<File> datFileList = Arrays.stream(historyDir.listFiles())
                .filter(PlayerHistoryManager::isDatFile).toList();
        for (File file : datFileList) {
            try {
                PlayerHistory playerHistory = PlayerHistory.readFromNbt(NbtIo.readCompressed(file));
                UUID playerUuid = UUID.fromString(file.getName().replace(".dat", ""));
                PlayerHistoryManager.histories.put(playerUuid, playerHistory);

            } catch (NullPointerException | IOException ignored) {
                CobblemonTrainerBattle.LOGGER.error("An error occurred while loading from {}", file.getName());
            }
        }

        CobblemonTrainerBattle.LOGGER.info("Loaded player battle history registry");
    }

    public static void saveToNbt(MinecraftServer server) {
        File historyDir = getTrainerBattleHistoryDir(server);

        if (!historyDir.exists()) {
            historyDir.mkdirs();
        }

        for (Map.Entry<UUID, PlayerHistory> historyEntry: PlayerHistoryManager.histories.entrySet()) {
            try {
                UUID playerUuid = historyEntry.getKey();
                PlayerHistory playerHistory = historyEntry.getValue();

                File newPlayerHistory = new File(historyDir, String.format("%s.dat", playerUuid));
                File oldPlayerHistory = new File(historyDir, String.format("%s.dat_old", playerUuid));

                if (newPlayerHistory.exists()) {
                    newPlayerHistory.renameTo(oldPlayerHistory);
                }

                NbtIo.writeCompressed(playerHistory.writeToNbt(new NbtCompound()), newPlayerHistory);

            } catch (IOException e) {
                UUID playerUuid = historyEntry.getKey();
                CobblemonTrainerBattle.LOGGER.error("An error occurred while saving to {}.dat", playerUuid);
            }
        }

        CobblemonTrainerBattle.LOGGER.info("Saved player battle history registry");
    }

    private static File getTrainerBattleHistoryDir(MinecraftServer server) {
        File worldDir = server.getSavePath(WorldSavePath.ROOT).toFile();

        return new File(worldDir, CobblemonTrainerBattle.MOD_ID);
    }

    private static boolean isDatFile(File file) {
        return file.getName().endsWith(".dat");
    }
}
