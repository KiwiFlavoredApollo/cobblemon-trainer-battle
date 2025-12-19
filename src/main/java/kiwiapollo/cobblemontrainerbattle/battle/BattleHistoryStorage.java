package kiwiapollo.cobblemontrainerbattle.battle;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class BattleHistoryStorage implements ServerLifecycleEvents.ServerStarted, ServerLifecycleEvents.ServerStopped, ServerTickEvents.EndTick {
    private static final String BATTLE_HISTORY = "battle_history";
    private static final int SAVE_INTERVAL_IN_MINUTES = 20;

    private static BattleHistoryStorage instance;
    private static Map<UUID, Map<Identifier, BattleHistory>> storage;

    private BattleHistoryStorage() {
        storage = new HashMap<>();
    }

    public static BattleHistoryStorage getInstance() {
        if (instance == null) {
            instance = new BattleHistoryStorage();
        }

        return instance;
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

    private int getSaveIntervalInTicks() {
        return SAVE_INTERVAL_IN_MINUTES * SharedConstants.TICKS_PER_MINUTE;
    }

    public BattleHistory get(UUID player, Identifier trainer) {
        if (!storage.containsKey(player)) {
            storage.put(player, new HashMap<>());
        }
        
        if (!storage.get(player).containsKey(trainer)) {
            storage.get(player).put(trainer, new BattleHistory());
        }

        return storage.get(player).get(trainer);
    }

    public int getTotalTrainerVictoryCount(UUID player) {
        return storage.get(player).values().stream()
                .map(BattleHistory::getVictoryCount)
                .reduce(Integer::sum).orElse(0);
    }

    public int getTotalTrainerKillCount(UUID player) {
        return storage.get(player).values().stream()
                .map(BattleHistory::getKillCount)
                .reduce(Integer::sum).orElse(0);
    }

    private void load(MinecraftServer server) {
        CobblemonTrainerBattle.LOGGER.info("Loading battle histories...");

        storage.clear();

        for (File file : getBattleHistoryFiles(server)) {
            try {
                UUID uuid = UUID.fromString(file.getName().replace(".dat", ""));
                storage.put(uuid, toMap(NbtIo.readCompressed(file)));

            } catch (NullPointerException | IOException e) {
                UUID uuid = UUID.fromString(file.getName().replace(".dat", ""));
                File backup = new File(getBattleHistoryDirectory(server), uuid + ".dat_bak");
                file.renameTo(backup);

                CobblemonTrainerBattle.LOGGER.error("Failed to load battle history: {}", file.getName());
                CobblemonTrainerBattle.LOGGER.info("Backup created: {}", backup.getName());
            }
        }

        CobblemonTrainerBattle.LOGGER.info("Loaded battle histories");
    }

    private List<File> getBattleHistoryFiles(MinecraftServer server) {
        try {
            return List.of(getBattleHistoryDirectory(server).listFiles(new DatFileFilter()));

        } catch (NullPointerException e) {
            return List.of();
        }
    }

    private Map<Identifier, BattleHistory> toMap(NbtCompound nbt) {
        Map<Identifier, BattleHistory> map = new HashMap<>();

        for (String trainer : nbt.getKeys()) {
            try {
                BattleHistory history = new BattleHistory();
                history.readFromNbt(nbt.getCompound(trainer));

                map.put(toDefaultedIdentifier(trainer), history);

            } catch (NullPointerException | IllegalArgumentException ignored) {

            }
        }

        return map;
    }

    private Identifier toDefaultedIdentifier(String string) {
        if (string.contains(String.valueOf(Identifier.NAMESPACE_SEPARATOR))) {
            return Identifier.tryParse(string);

        } else {
            return Identifier.of(CobblemonTrainerBattle.MOD_ID, string);
        }
    }

    private void save(MinecraftServer server) {
        CobblemonTrainerBattle.LOGGER.info("Saving battle histories...");

        for (Map.Entry<UUID, Map<Identifier, BattleHistory>> entry: storage.entrySet()) {
            try {
                UUID uuid = entry.getKey();
                Map<Identifier, BattleHistory> map = entry.getValue();

                Files.createDirectories(getBattleHistoryDirectory(server).toPath());

                File newFile = new File(getBattleHistoryDirectory(server), uuid + ".dat");
                File oldFile = new File(getBattleHistoryDirectory(server), uuid + ".dat_old");

                if (newFile.exists()) {
                    newFile.renameTo(oldFile);
                }

                NbtIo.writeCompressed(toNbtCompound(map), newFile);

            } catch (IOException e) {
                UUID uuid = entry.getKey();

                File file = new File(getBattleHistoryDirectory(server), uuid + ".dat");
                CobblemonTrainerBattle.LOGGER.error("Failed to save battle history: {}", file.getName());
            }
        }

        CobblemonTrainerBattle.LOGGER.info("Saved battle histories");
    }

    private NbtCompound toNbtCompound(Map<Identifier, BattleHistory> map) {
        NbtCompound nbt = new NbtCompound();

        for (Map.Entry<Identifier, BattleHistory> entry : map.entrySet()) {
            try {
                Identifier trainer = entry.getKey();
                BattleHistory history = entry.getValue();

                nbt.put(trainer.toString(), history.writeToNbt(new NbtCompound()));

            } catch (NullPointerException ignored) {

            }
        }

        return nbt;
    }

    private File getBattleHistoryDirectory(MinecraftServer server) {
        File parent = server.getSavePath(WorldSavePath.ROOT).toFile();
        String child = CobblemonTrainerBattle.MOD_ID + "/" + BATTLE_HISTORY;

        return new File(parent, child);
    }

    private static class DatFileFilter implements FileFilter {
        @Override
        public boolean accept(File file) {
            return file.getName().endsWith(".dat");
        }
    }

    public static class Renamer implements ServerLifecycleEvents.ServerStarted {
        @Override
        public void onServerStarted(MinecraftServer server) {
            File oldDirectory = getOldBattleHistoryDirectory(server);
            File newDirectory = getNewBattleHistoryDirectory(server);

            oldDirectory.renameTo(newDirectory);
        }

        private File getOldBattleHistoryDirectory(MinecraftServer server) {
            final String OLD_BATTLE_HISTORY = "history";
            File parent = server.getSavePath(WorldSavePath.ROOT).toFile();
            String child = CobblemonTrainerBattle.MOD_ID + "/" + OLD_BATTLE_HISTORY;

            return new File(parent, child);
        }

        private File getNewBattleHistoryDirectory(MinecraftServer server) {
            final String NEW_BATTLE_HISTORY = BATTLE_HISTORY;
            File parent = server.getSavePath(WorldSavePath.ROOT).toFile();
            String child = CobblemonTrainerBattle.MOD_ID + "/" + NEW_BATTLE_HISTORY;

            return new File(parent, child);
        }
    }
}
