package kiwiapollo.cobblemontrainerbattle.parser.history;

import kiwiapollo.cobblemontrainerbattle.common.LazyMap;
import net.minecraft.nbt.NbtCompound;

import java.util.*;

public class PlayerHistory implements NbtConvertible, RecordStatisticsProvider, LazyMap<String, TrainerRecord> {
    private final Map<String, TrainerRecord> records;

    public PlayerHistory() {
        this.records = new HashMap<>();
    }

    @Override
    public TrainerRecord getOrCreate(String trainer) {
        if (!records.containsKey(trainer)) {
            records.put(trainer, new TrainerRecord());
        }

        return records.get(trainer);
    }

    @Override
    public void put(String trainer, TrainerRecord record) {
        records.put(trainer, record);
    }

    @Override
    public void clear() {
        records.clear();
    }

    @Override
    public void remove(String trainer) {
        records.remove(trainer);
    }

    @Override
    public Iterable<? extends Map.Entry<String, TrainerRecord>> entrySet() {
        return records.entrySet();
    }

    @Override
    public int getTotalTrainerVictoryCount() {
        return records.values().stream()
                .filter(record -> record instanceof TrainerRecord)
                .map(record -> (TrainerRecord) record)
                .map(TrainerRecord::getVictoryCount)
                .reduce(Integer::sum).orElse(0);
    }

    @Override
    public int getTotalTrainerKillCount() {
        return records.values().stream()
                .filter(record -> record instanceof EntityRecord)
                .map(record -> (EntityRecord) record)
                .map(EntityRecord::getKillCount)
                .reduce(Integer::sum).orElse(0);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        records.clear();

        for (String trainer : nbt.getKeys()) {
            try {
                TrainerRecord record = new TrainerRecord();
                record.readFromNbt(nbt.getCompound(trainer));
                records.put(toNonLegacyTrainer(trainer), record);

            } catch (NullPointerException | IllegalArgumentException ignored) {

            }
        }
    }

    private String toNonLegacyTrainer(String trainer) {
        return trainer.replace("^trainer:", "");
    }

    @Override
    public NbtCompound writeToNbt(NbtCompound nbt) {
        records.forEach((identifier, record) -> nbt.put(identifier, toNbtCompound(record)));
        return nbt;
    }

    private NbtCompound toNbtCompound(NbtConvertible record) {
        NbtCompound nbt = new NbtCompound();
        record.writeToNbt(nbt);
        return nbt;
    }
}
