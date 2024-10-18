package kiwiapollo.cobblemontrainerbattle.parser;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.WorldSavePath;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TrainerBattleHistoryRegistryParser {
    private static final int SAVE_INTERVAL = 100;

    public static void onEndWorldTick(ServerWorld world) {
        if (world.getServer().getTicks() % SAVE_INTERVAL == 0) {
            saveToNbt(world.getServer());
        }
    }

    public static void loadFromNbt(MinecraftServer server) {
        File historyDir = getTrainerBattleHistoryDir(server);

        if (!historyDir.isDirectory()) {
            return;
        }

        CobblemonTrainerBattle.trainerBattleHistoryRegistry.clear();
        List<File> datFileList = Arrays.stream(historyDir.listFiles())
                .filter(TrainerBattleHistoryRegistryParser::isDatFile).toList();
        for (File file : datFileList) {
            try {
                TrainerBattleHistory playerHistory = TrainerBattleHistory.readFromNbt(NbtIo.readCompressed(file));
                UUID playerUuid = UUID.fromString(file.getName().replace(".dat", ""));
                CobblemonTrainerBattle.trainerBattleHistoryRegistry.put(playerUuid, playerHistory);

            } catch (NullPointerException | IOException ignored) {
                CobblemonTrainerBattle.LOGGER.error("An error occurred while loading from {}", file.getName());
            }
        }
    }

    public static void saveToNbt(MinecraftServer server) {
        File historyDir = getTrainerBattleHistoryDir(server);

        if (!historyDir.exists()) {
            historyDir.mkdirs();
        }

        for (Map.Entry<UUID, TrainerBattleHistory> historyEntry: CobblemonTrainerBattle.trainerBattleHistoryRegistry.entrySet()) {
            try {
                UUID playerUuid = historyEntry.getKey();
                TrainerBattleHistory trainerBattleHistory = historyEntry.getValue();

                File newPlayerHistory = new File(historyDir, String.format("%s.dat", playerUuid));
                File oldPlayerHistory = new File(historyDir, String.format("%s.dat_old", playerUuid));

                if (newPlayerHistory.exists()) {
                    newPlayerHistory.renameTo(oldPlayerHistory);
                }

                NbtIo.writeCompressed(trainerBattleHistory.writeToNbt(new NbtCompound()), newPlayerHistory);

            } catch (IOException e) {
                UUID playerUuid = historyEntry.getKey();
                CobblemonTrainerBattle.LOGGER.error("An error occurred while saving to {}.dat", playerUuid);
            }
        }
    }

    private static File getTrainerBattleHistoryDir(MinecraftServer server) {
        File worldDir = server.getSavePath(WorldSavePath.ROOT).toFile();

        return new File(worldDir, CobblemonTrainerBattle.NAMESPACE);
    }

    private static boolean isDatFile(File file) {
        return file.getName().endsWith(".dat");
    }
}
