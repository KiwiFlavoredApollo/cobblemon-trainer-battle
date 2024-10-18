package kiwiapollo.cobblemontrainerbattle.common;

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

        record.victoryCount += 1;
        record.lastBattleDate = Instant.now();

        recordRegistry.put(trainer, record);
    }

    public void addPlayerDefeat(Identifier trainer) {
        TrainerBattleRecord record = getTrainerBattleRecord(trainer);

        record.defeatCount += 1;
        record.lastBattleDate = Instant.now();

        recordRegistry.put(trainer, record);
    }

    private TrainerBattleRecord getTrainerBattleRecord(Identifier trainer) {
        if (recordRegistry.containsKey(trainer)) {
            return recordRegistry.get(trainer);
        } else {
            return new TrainerBattleRecord();
        }
    }

    public void remove(Identifier trainer) {
        recordRegistry.remove(trainer);
    }

    public boolean isTrainerDefeated(Identifier trainer) {
        if (!recordRegistry.containsKey(trainer)) {
            return false;
        } else {
            return recordRegistry.get(trainer).victoryCount > 0;
        }
    }

    private void put(Identifier identifier, TrainerBattleRecord trainerBattleRecord) {
        recordRegistry.put(identifier, trainerBattleRecord);
    }

    public NbtCompound writeToNbt(NbtCompound nbt) {
        for (Map.Entry<Identifier, TrainerBattleRecord> trainerBattleRecordEntry: recordRegistry.entrySet()) {
            Identifier identifier = trainerBattleRecordEntry.getKey();
            TrainerBattleRecord trainerBattleRecord = trainerBattleRecordEntry.getValue();

            nbt.put(identifier.toString(), writeTrainerBattleRecordToNbt(trainerBattleRecord));
        }
        return nbt;
    }

    private NbtCompound writeTrainerBattleRecordToNbt(TrainerBattleRecord trainerBattleRecord) {
        NbtCompound subNbt = new NbtCompound();

        subNbt.putLong("lastBattleDate", trainerBattleRecord.lastBattleDate.toEpochMilli());
        subNbt.putInt("victoryCount", trainerBattleRecord.victoryCount);
        subNbt.putInt("defeatCount", trainerBattleRecord.defeatCount);

        return subNbt;
    }

    public static TrainerBattleHistory readFromNbt(NbtCompound nbt) {
        TrainerBattleHistory history = new TrainerBattleHistory();
        for (String string : nbt.getKeys()) {
            history.put(
                    new Identifier(string),
                    readTrainerBattleRecordFromNbt(nbt.getCompound(string))
            );
        }
        return history;
    }

    private static TrainerBattleRecord readTrainerBattleRecordFromNbt(NbtCompound compound) {
        return new TrainerBattleRecord(
                Instant.ofEpochMilli(compound.getLong("lastBattleDate")),
                compound.getInt("victoryCount"),
                compound.getInt("defeatCount")
        );
    }
}
