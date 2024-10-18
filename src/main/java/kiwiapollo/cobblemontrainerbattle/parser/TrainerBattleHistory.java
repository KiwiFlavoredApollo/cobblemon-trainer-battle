package kiwiapollo.cobblemontrainerbattle.parser;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.time.Instant;
import java.util.*;

public class TrainerBattleHistory {

    private final Map<Identifier, TrainerBattleHistoryRecord> recordRegistry;

    public TrainerBattleHistory() {
        recordRegistry = new HashMap<>();
    }

    public void addPlayerVictory(Identifier trainer) {
        TrainerBattleHistoryRecord record = getTrainerBattleRecord(trainer);

        record.victory += 1;
        record.timestamp = Instant.now();

        recordRegistry.put(trainer, record);
    }

    public void addPlayerDefeat(Identifier trainer) {
        TrainerBattleHistoryRecord record = getTrainerBattleRecord(trainer);

        record.defeat += 1;
        record.timestamp = Instant.now();

        recordRegistry.put(trainer, record);
    }

    private TrainerBattleHistoryRecord getTrainerBattleRecord(Identifier trainer) {
        if (recordRegistry.containsKey(trainer)) {
            return recordRegistry.get(trainer);
        } else {
            return new TrainerBattleHistoryRecord();
        }
    }

    public boolean isTrainerDefeated(Identifier trainer) {
        if (!recordRegistry.containsKey(trainer)) {
            return false;
        } else {
            return recordRegistry.get(trainer).victory > 0;
        }
    }

    private void put(Identifier identifier, TrainerBattleHistoryRecord record) {
        recordRegistry.put(identifier, record);
    }

    public void remove(Identifier trainer) {
        recordRegistry.remove(trainer);
    }

    public NbtCompound writeToNbt(NbtCompound nbt) {
        for (Map.Entry<Identifier, TrainerBattleHistoryRecord> recordEntry: recordRegistry.entrySet()) {
            Identifier trainer = recordEntry.getKey();
            TrainerBattleHistoryRecord record = recordEntry.getValue();

            nbt.put(trainer.toString(), writeTrainerBattleRecordToNbt(record));
        }
        return nbt;
    }

    private NbtCompound writeTrainerBattleRecordToNbt(TrainerBattleHistoryRecord record) {
        NbtCompound nbt = new NbtCompound();

        nbt.putLong("timestamp", record.timestamp.toEpochMilli());
        nbt.putInt("victory", record.victory);
        nbt.putInt("defeat", record.defeat);

        return nbt;
    }

    public static TrainerBattleHistory readFromNbt(NbtCompound nbt) {
        TrainerBattleHistory history = new TrainerBattleHistory();
        for (String trainer : nbt.getKeys()) {
            history.put(
                    new Identifier(trainer),
                    readTrainerBattleRecordFromNbt(nbt.getCompound(trainer))
            );
        }
        return history;
    }

    private static TrainerBattleHistoryRecord readTrainerBattleRecordFromNbt(NbtCompound nbt) {
        return new TrainerBattleHistoryRecord(
                Instant.ofEpochMilli(nbt.getLong("timestamp")),
                nbt.getInt("victory"),
                nbt.getInt("defeat")
        );
    }
}
