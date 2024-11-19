package kiwiapollo.cobblemontrainerbattle.parser.history;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.*;

public class PlayerHistory {
    private final Map<Identifier, PlayerHistoryRecord> records;

    public PlayerHistory() {
        this.records = new HashMap<>();
    }

    public PlayerHistoryRecord getOrCreateRecord(Identifier identifier) throws IllegalArgumentException {
        if (!records.containsKey(identifier)) {
            records.put(identifier, createRecord(identifier));
        }

        return records.get(identifier);
    }

    public int getTotalTrainerVictoryCount() {
        return records.values().stream()
                .filter(record -> record instanceof TrainerRecord)
                .map(record -> (TrainerRecord) record)
                .map(TrainerRecord::getVictoryCount)
                .reduce(Integer::sum).orElse(0);
    }

    public int getTotalTrainerKillCount() {
        return records.values().stream()
                .filter(record -> record instanceof EntityRecord)
                .map(record -> (EntityRecord) record)
                .map(EntityRecord::getKillCount)
                .reduce(Integer::sum).orElse(0);
    }

    public void readFromNbt(NbtCompound nbt) {
        records.clear();

        List<Identifier> identifiers = nbt.getKeys().stream()
                .map(Identifier::tryParse)
                .filter(Objects::nonNull).toList();

        for (Identifier identifier : identifiers) {
            try {
                PlayerHistoryRecord record = createRecord(identifier);
                record.readFromNbt(nbt.getCompound(identifier.toString()));

                records.put(identifier, record);

            } catch (NullPointerException | IllegalArgumentException ignored) {

            }
        }
    }

    public void writeToNbt(NbtCompound nbt) {
        records.forEach((identifier, record) -> {
            nbt.put(identifier.toString(), toNbtCompound(record));
        });
    }

    private NbtCompound toNbtCompound(PlayerHistoryRecord record) {
        NbtCompound nbt = new NbtCompound();
        record.writeToNbt(nbt);
        return nbt;
    }

    private PlayerHistoryRecord createRecord(Identifier identifier) {
        if (identifier.getNamespace().equals("trainer")) {
            return new TrainerRecord();

        } else if (identifier.getNamespace().equals("group")) {
            return new TrainerGroupRecord();

        } else if (identifier.toString().equals("minigame:battlefactory")) {
            return new BattleFactoryRecord();

        } else {
            throw new IllegalArgumentException();
        }
    }
}
