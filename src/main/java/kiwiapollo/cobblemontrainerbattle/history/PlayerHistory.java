package kiwiapollo.cobblemontrainerbattle.history;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.*;

public class PlayerHistory {
    private final Map<Identifier, TrainerRecord> records;

    public PlayerHistory() {
        this.records = new HashMap<>();
    }

    public TrainerRecord get(Identifier trainer) {
        if (!records.containsKey(trainer)) {
            records.put(trainer, new TrainerRecord());
        }

        return records.get(trainer);
    }

    public int getTotalTrainerVictoryCount() {
        return records.values().stream()
                .map(TrainerRecord::getVictoryCount)
                .reduce(Integer::sum).orElse(0);
    }

    public int getTotalTrainerKillCount() {
        return records.values().stream()
                .map(TrainerRecord::getKillCount)
                .reduce(Integer::sum).orElse(0);
    }

    public void readFromNbt(NbtCompound nbt) {
        records.clear();

        for (String trainer : nbt.getKeys()) {
            try {
                TrainerRecord record = new TrainerRecord();
                record.readFromNbt(nbt.getCompound(trainer));
                records.put(toDefaultedIdentifier(trainer), record);

            } catch (NullPointerException | IllegalArgumentException ignored) {

            }
        }
    }

    public NbtCompound writeToNbt(NbtCompound nbt) {
        for (Map.Entry<Identifier, TrainerRecord> entry : records.entrySet()) {
            try {
                Identifier identifier = entry.getKey();
                TrainerRecord record = entry.getValue();
                nbt.put(identifier.toString(), toNbtCompound(record));

            } catch (NullPointerException ignored) {

            }
        }

        return nbt;
    }

    private NbtCompound toNbtCompound(TrainerRecord record) {
        NbtCompound nbt = new NbtCompound();
        record.writeToNbt(nbt);
        return nbt;
    }

    private Identifier toDefaultedIdentifier(String string) {
        if (string.contains(String.valueOf(Identifier.NAMESPACE_SEPARATOR))) {
            return Identifier.tryParse(string);

        } else {
            return Identifier.of(CobblemonTrainerBattle.MOD_ID, string);
        }
    }
}
