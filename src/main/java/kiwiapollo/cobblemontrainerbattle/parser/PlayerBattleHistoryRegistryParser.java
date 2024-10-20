package kiwiapollo.cobblemontrainerbattle.parser;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerBattleHistoryRegistryParser {
    private static final int SAVE_INTERVAL = 24000;

    public static void onEndServerTick(MinecraftServer server) {
        if (server.getTicks() % SAVE_INTERVAL == 0) {
            saveToNbt(server);
        }
    }

    public static void loadFromNbt(MinecraftServer server) {
        File historyDir = getTrainerBattleHistoryDir(server);

        if (!historyDir.isDirectory()) {
            return;
        }

        CobblemonTrainerBattle.playerBattleHistoryRegistry.clear();
        List<File> datFileList = Arrays.stream(historyDir.listFiles())
                .filter(PlayerBattleHistoryRegistryParser::isDatFile).toList();
        for (File file : datFileList) {
            try {
                PlayerBattleHistory playerHistory = PlayerBattleHistory.readFromNbt(NbtIo.readCompressed(file));
                UUID playerUuid = UUID.fromString(file.getName().replace(".dat", ""));
                CobblemonTrainerBattle.playerBattleHistoryRegistry.put(playerUuid, playerHistory);

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

        for (Map.Entry<UUID, PlayerBattleHistory> historyEntry: CobblemonTrainerBattle.playerBattleHistoryRegistry.entrySet()) {
            try {
                UUID playerUuid = historyEntry.getKey();
                PlayerBattleHistory playerBattleHistory = historyEntry.getValue();

                File newPlayerHistory = new File(historyDir, String.format("%s.dat", playerUuid));
                File oldPlayerHistory = new File(historyDir, String.format("%s.dat_old", playerUuid));

                if (newPlayerHistory.exists()) {
                    newPlayerHistory.renameTo(oldPlayerHistory);
                }

                NbtIo.writeCompressed(playerBattleHistory.writeToNbt(new NbtCompound()), newPlayerHistory);

            } catch (IOException e) {
                UUID playerUuid = historyEntry.getKey();
                CobblemonTrainerBattle.LOGGER.error("An error occurred while saving to {}.dat", playerUuid);
            }
        }

        CobblemonTrainerBattle.LOGGER.info("Saved player battle history registry");
    }

    private static File getTrainerBattleHistoryDir(MinecraftServer server) {
        File worldDir = server.getSavePath(WorldSavePath.ROOT).toFile();

        return new File(worldDir, CobblemonTrainerBattle.NAMESPACE);
    }

    private static boolean isDatFile(File file) {
        return file.getName().endsWith(".dat");
    }
}
