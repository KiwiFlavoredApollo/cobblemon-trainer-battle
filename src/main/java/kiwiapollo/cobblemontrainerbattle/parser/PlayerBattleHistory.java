package kiwiapollo.cobblemontrainerbattle.parser;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.time.Instant;
import java.util.*;

public class PlayerBattleHistory {

    private final Map<Identifier, PlayerBattleRecord> recordRegistry;

    public PlayerBattleHistory() {
        recordRegistry = new HashMap<>();
    }

    public void addPlayerVictory(Identifier opponent) {
        PlayerBattleRecord record = getPlayerBattleRecord(opponent);

        record.victory += 1;
        record.timestamp = Instant.now();

        recordRegistry.put(opponent, record);
    }

    public void addPlayerDefeat(Identifier opponent) {
        PlayerBattleRecord record = getPlayerBattleRecord(opponent);

        record.defeat += 1;
        record.timestamp = Instant.now();

        recordRegistry.put(opponent, record);
    }

    private PlayerBattleRecord getPlayerBattleRecord(Identifier opponent) {
        if (recordRegistry.containsKey(opponent)) {
            return recordRegistry.get(opponent);
        } else {
            return new PlayerBattleRecord();
        }
    }

    public boolean isOpponentDefeated(Identifier opponent) {
        if (!recordRegistry.containsKey(opponent)) {
            return false;
        } else {
            return recordRegistry.get(opponent).victory > 0;
        }
    }

    private void put(Identifier identifier, PlayerBattleRecord record) {
        recordRegistry.put(identifier, record);
    }

    public void remove(Identifier opponent) {
        recordRegistry.remove(opponent);
    }

    public NbtCompound writeToNbt(NbtCompound nbt) {
        for (Map.Entry<Identifier, PlayerBattleRecord> recordEntry: recordRegistry.entrySet()) {
            Identifier trainer = recordEntry.getKey();
            PlayerBattleRecord record = recordEntry.getValue();

            nbt.put(trainer.toString(), writeTrainerBattleRecordToNbt(record));
        }
        return nbt;
    }

    private NbtCompound writeTrainerBattleRecordToNbt(PlayerBattleRecord record) {
        NbtCompound nbt = new NbtCompound();

        nbt.putLong("timestamp", record.timestamp.toEpochMilli());
        nbt.putInt("victory", record.victory);
        nbt.putInt("defeat", record.defeat);

        return nbt;
    }

    public static PlayerBattleHistory readFromNbt(NbtCompound nbt) {
        PlayerBattleHistory history = new PlayerBattleHistory();
        for (String opponent : nbt.getKeys()) {
            history.put(
                    new Identifier(opponent),
                    readTrainerBattleRecordFromNbt(nbt.getCompound(opponent))
            );
        }
        return history;
    }

    private static PlayerBattleRecord readTrainerBattleRecordFromNbt(NbtCompound nbt) {
        return new PlayerBattleRecord(
                Instant.ofEpochMilli(nbt.getLong("timestamp")),
                nbt.getInt("victory"),
                nbt.getInt("defeat")
        );
    }
}
