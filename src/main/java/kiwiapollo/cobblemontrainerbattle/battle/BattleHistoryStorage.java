package kiwiapollo.cobblemontrainerbattle.battle;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

public class BattleHistoryStorage implements ServerLifecycleEvents.ServerStarted, ServerLifecycleEvents.ServerStopped, ServerTickEvents.EndTick {
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

    public BattleHistory get(ServerPlayerEntity player, Identifier trainer) {
        if (!storage.containsKey(player.getUuid())) {
            storage.put(player.getUuid(), new HashMap<>());
        }
        
        if (!storage.get(player.getUuid()).containsKey(trainer)) {
            storage.get(player.getUuid()).put(trainer, new BattleHistory());
        }

        return storage.get(player.getUuid()).get(trainer);
    }

    public int getTotalTrainerVictoryCount(ServerPlayerEntity player) {
        return storage.get(player.getUuid()).values().stream()
                .map(BattleHistory::getVictoryCount)
                .reduce(Integer::sum).orElse(0);
    }

    public int getTotalTrainerKillCount(ServerPlayerEntity player) {
        return storage.get(player.getUuid()).values().stream()
                .map(BattleHistory::getKillCount)
                .reduce(Integer::sum).orElse(0);
    }

    // TODO
    // 매개변수 이렇게 하는 것이 맞는지 고민해보기
    public void readFromNbt(UUID uuid, NbtCompound nbt) {
        for (String trainer : nbt.getKeys()) {
            try {
                BattleHistory history = new BattleHistory();
                history.readFromNbt(nbt.getCompound(trainer));
                storage.get(uuid).put(toDefaultedIdentifier(trainer), history);

            } catch (NullPointerException | IllegalArgumentException ignored) {

            }
        }
    }

    public NbtCompound writeToNbt(UUID uuid, NbtCompound nbt) {
        for (Map.Entry<Identifier, BattleHistory> entry : storage.get(uuid).entrySet()) {
            try {
                Identifier identifier = entry.getKey();
                BattleHistory history = entry.getValue();
                nbt.put(identifier.toString(), toNbtCompound(history));

            } catch (NullPointerException ignored) {

            }
        }

        return nbt;
    }

    private NbtCompound toNbtCompound(BattleHistory history) {
        NbtCompound nbt = new NbtCompound();
        history.writeToNbt(nbt);
        return nbt;
    }

    private Identifier toDefaultedIdentifier(String string) {
        if (string.contains(String.valueOf(Identifier.NAMESPACE_SEPARATOR))) {
            return Identifier.tryParse(string);

        } else {
            return Identifier.of(CobblemonTrainerBattle.MOD_ID, string);
        }
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

                readFromNbt(uuid, NbtIo.readCompressed(file));

            } catch (NullPointerException | IOException e) {
                UUID uuid = UUID.fromString(file.getName().replace(".dat", ""));

                File backup = new File(historyPath, String.format("%s.dat_bak", uuid));
                file.renameTo(backup);

                storage.put(uuid, new HashMap<>());

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

        for (Map.Entry<UUID, Map<Identifier, BattleHistory>> entry: storage.entrySet()) {
            try {
                UUID uuid = entry.getKey();

                File newHistory = new File(historyPath, String.format("%s.dat", uuid));
                File oldHistory = new File(historyPath, String.format("%s.dat_old", uuid));

                if (newHistory.exists()) {
                    newHistory.renameTo(oldHistory);
                }

                NbtIo.writeCompressed(writeToNbt(uuid, new NbtCompound()), newHistory);

            } catch (IOException e) {
                UUID uuid = entry.getKey();
                File file = new File(historyPath, String.format("%s.dat", uuid));
                CobblemonTrainerBattle.LOGGER.error("Error occurred while saving {}", file.getName());
            }
        }

        CobblemonTrainerBattle.LOGGER.info("Saved player histories");
    }
}
