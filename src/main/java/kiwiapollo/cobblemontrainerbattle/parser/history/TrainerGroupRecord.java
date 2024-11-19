package kiwiapollo.cobblemontrainerbattle.parser.history;

import net.minecraft.nbt.NbtCompound;

import java.time.Instant;

public class TrainerGroupRecord implements PlayerHistoryRecord, BattleRecord {
    private int victory;
    private int defeat;
    private Instant timestamp;

    public TrainerGroupRecord() {
        this.victory = 0;
        this.defeat = 0;
        this.timestamp = Instant.now();
    }

    @Override
    public int getVictoryCount() {
        return victory;
    }

    @Override
    public void setVictoryCount(int count) {
        victory = count;
        updateTimestamp();
    }

    @Override
    public int getDefeatCount() {
        return defeat;
    }

    @Override
    public void setDefeatCount(int count) {
        defeat = count;
        updateTimestamp();
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        timestamp = Instant.ofEpochMilli(nbt.getLong("timestamp"));
        victory = nbt.getInt("victory");
        defeat = nbt.getInt("defeat");
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        nbt.putLong("timestamp", timestamp.toEpochMilli());
        nbt.putInt("victory", victory);
        nbt.putInt("defeat", defeat);
    }

    private void updateTimestamp() {
        timestamp = Instant.now();
    }
}
