package kiwiapollo.cobblemontrainerbattle.global.history;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.LazyMap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.*;

public class PlayerHistory implements NbtConvertible, RecordStatisticsProvider, LazyMap<Identifier, TrainerRecord> {
    private final Map<Identifier, TrainerRecord> records;

    public PlayerHistory() {
        this.records = new HashMap<>();
    }

    @Override
    public TrainerRecord getOrCreate(Identifier trainer) {
        if (!records.containsKey(trainer)) {
            records.put(trainer, new TrainerRecord());
        }

        return records.get(trainer);
    }

    @Override
    public void put(Identifier trainer, TrainerRecord record) {
        records.put(trainer, record);
    }

    @Override
    public void clear() {
        records.clear();
    }

    @Override
    public void remove(Identifier trainer) {
        records.remove(trainer);
    }

    @Override
    public Iterable<? extends Map.Entry<Identifier, TrainerRecord>> entrySet() {
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
                records.put(toDefaultedIdentifier(trainer), record);

            } catch (NullPointerException | IllegalArgumentException ignored) {

            }
        }
    }

    @Override
    public NbtCompound writeToNbt(NbtCompound nbt) {
        records.forEach((identifier, record) -> nbt.put(identifier.toString(), toNbtCompound(record)));
        return nbt;
    }

    private NbtCompound toNbtCompound(NbtConvertible record) {
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
