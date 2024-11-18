package kiwiapollo.cobblemontrainerbattle.parser.history;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.*;

public class PlayerHistory {
    private final Map<Identifier, PlayerHistoryRecord> records;

    public PlayerHistory() {
        this.records = new HashMap<>();
    }

    public PlayerHistoryRecord get(Identifier identifier) {
        if (!records.containsKey(identifier)) {
            records.put(identifier, createPlayerHistoryRecord(identifier.toString()));
        }

        return records.get(identifier);
    }

    public void put(Identifier identifier, PlayerHistoryRecord record) {
        records.put(identifier, record);
    }

    public void remove(Identifier identifier) {
        records.remove(identifier);
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

    public NbtCompound writeToNbt(NbtCompound nbt) {
        for (Map.Entry<Identifier, ? extends PlayerHistoryRecord> recordEntry: records.entrySet()) {
            Identifier identifier = recordEntry.getKey();
            PlayerHistoryRecord record = recordEntry.getValue();

            nbt.put(identifier.toString(), record.writeToNbt(new NbtCompound()));
        }
        return nbt;
    }

    public static PlayerHistory readFromNbt(NbtCompound nbt) {
        PlayerHistory history = new PlayerHistory();
        for (String identifier : nbt.getKeys()) {
            try {
                NbtCompound record = nbt.getCompound(identifier);
                history.get(Identifier.tryParse(identifier)).readFromNbt(record);

            } catch (IllegalArgumentException ignored) {
                CobblemonTrainerBattle.LOGGER.error("Unable to load record : {}", identifier);
            }
        }
        return history;
    }

    private static PlayerHistoryRecord createPlayerHistoryRecord(String identifier) {
        if (identifier.matches("^trainer:.+")) {
            return new TrainerRecord();

        } else if (identifier.matches("^group:.+")) {
            return new TrainerGroupRecord();

        } else if (identifier.equals("minigame:battlefactory")) {
            return new BattleFactoryRecord();

        } else {
            throw new IllegalArgumentException();
        }
    }
}
