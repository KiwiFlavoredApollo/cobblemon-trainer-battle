package kiwiapollo.cobblemontrainerbattle.parser;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.time.Instant;
import java.util.*;

public class TrainerBattleHistory {

    private final Map<Identifier, TrainerBattleRecord> recordRegistry;

    public TrainerBattleHistory() {
        recordRegistry = new HashMap<>();
    }

    public void addPlayerVictory(Identifier trainer) {
        TrainerBattleRecord record = getTrainerBattleRecord(trainer);

        record.victory += 1;
        record.timestamp = Instant.now();

        recordRegistry.put(trainer, record);
    }

    public void addPlayerDefeat(Identifier trainer) {
        TrainerBattleRecord record = getTrainerBattleRecord(trainer);

        record.defeat += 1;
        record.timestamp = Instant.now();

        recordRegistry.put(trainer, record);
    }

    private TrainerBattleRecord getTrainerBattleRecord(Identifier trainer) {
        if (recordRegistry.containsKey(trainer)) {
            return recordRegistry.get(trainer);
        } else {
            return new TrainerBattleRecord();
        }
    }

    public boolean isTrainerDefeated(Identifier trainer) {
        if (!recordRegistry.containsKey(trainer)) {
            return false;
        } else {
            return recordRegistry.get(trainer).victory > 0;
        }
    }

    private void put(Identifier identifier, TrainerBattleRecord trainerBattleRecord) {
        recordRegistry.put(identifier, trainerBattleRecord);
    }

    public void remove(Identifier trainer) {
        recordRegistry.remove(trainer);
    }

    public NbtCompound writeToNbt(NbtCompound nbt) {
        for (Map.Entry<Identifier, TrainerBattleRecord> recordEntry: recordRegistry.entrySet()) {
            Identifier trainer = recordEntry.getKey();
            TrainerBattleRecord record = recordEntry.getValue();

            nbt.put(trainer.toString(), writeTrainerBattleRecordToNbt(record));
        }
        return nbt;
    }

    private NbtCompound writeTrainerBattleRecordToNbt(TrainerBattleRecord record) {
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

    private static TrainerBattleRecord readTrainerBattleRecordFromNbt(NbtCompound nbt) {
        return new TrainerBattleRecord(
                Instant.ofEpochMilli(nbt.getLong("timestamp")),
                nbt.getInt("victory"),
                nbt.getInt("defeat")
        );
    }
}
