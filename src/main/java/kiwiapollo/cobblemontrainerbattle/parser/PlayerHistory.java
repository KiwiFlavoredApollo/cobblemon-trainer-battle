package kiwiapollo.cobblemontrainerbattle.parser;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.time.Instant;
import java.util.*;

public class PlayerHistory {

    private final Map<Identifier, TrainerRecord> trainerRecordRegistry;

    public PlayerHistory() {
        trainerRecordRegistry = new HashMap<>();
    }

    public void addPlayerVictory(Identifier opponent) {
        TrainerRecord record = getPlayerBattleRecord(opponent);

        record.victory += 1;
        record.timestamp = Instant.now();

        trainerRecordRegistry.put(opponent, record);
    }

    public void addPlayerDefeat(Identifier opponent) {
        TrainerRecord record = getPlayerBattleRecord(opponent);

        record.defeat += 1;
        record.timestamp = Instant.now();

        trainerRecordRegistry.put(opponent, record);
    }

    public void addPlayerKill(Identifier opponent) {
        TrainerRecord record = getPlayerBattleRecord(opponent);

        record.kill += 1;
        record.timestamp = Instant.now();

        trainerRecordRegistry.put(opponent, record);
    }

    private TrainerRecord getPlayerBattleRecord(Identifier opponent) {
        if (trainerRecordRegistry.containsKey(opponent)) {
            return trainerRecordRegistry.get(opponent);
        } else {
            return new TrainerRecord();
        }
    }

    public boolean isOpponentDefeated(Identifier opponent) {
        if (!trainerRecordRegistry.containsKey(opponent)) {
            return false;
        } else {
            return trainerRecordRegistry.get(opponent).victory > 0;
        }
    }

    public int getTotalVictoryCount() {
        return trainerRecordRegistry.values().stream().map(record -> record.victory).reduce(Integer::sum).orElse(0);
    }

    public int getTotalKillCount() {
        return trainerRecordRegistry.values().stream().map(record -> record.kill).reduce(Integer::sum).orElse(0);
    }

    private void put(Identifier identifier, TrainerRecord record) {
        trainerRecordRegistry.put(identifier, record);
    }

    public void remove(Identifier opponent) {
        trainerRecordRegistry.remove(opponent);
    }

    public NbtCompound writeToNbt(NbtCompound nbt) {
        for (Map.Entry<Identifier, TrainerRecord> recordEntry: trainerRecordRegistry.entrySet()) {
            Identifier trainer = recordEntry.getKey();
            TrainerRecord record = recordEntry.getValue();

            nbt.put(trainer.toString(), writeTrainerBattleRecordToNbt(record));
        }
        return nbt;
    }

    private NbtCompound writeTrainerBattleRecordToNbt(TrainerRecord record) {
        NbtCompound nbt = new NbtCompound();

        nbt.putLong("timestamp", record.timestamp.toEpochMilli());
        nbt.putInt("victory", record.victory);
        nbt.putInt("defeat", record.defeat);
        nbt.putInt("kill", record.kill);

        return nbt;
    }

    public static PlayerHistory readFromNbt(NbtCompound nbt) {
        PlayerHistory history = new PlayerHistory();
        for (String opponent : nbt.getKeys()) {
            history.put(
                    new Identifier(opponent),
                    readTrainerBattleRecordFromNbt(nbt.getCompound(opponent))
            );
        }
        return history;
    }

    private static TrainerRecord readTrainerBattleRecordFromNbt(NbtCompound nbt) {
        return new TrainerRecord(
                Instant.ofEpochMilli(nbt.getLong("timestamp")),
                nbt.getInt("victory"),
                nbt.getInt("defeat"),
                nbt.getInt("kill")
        );
    }
}
