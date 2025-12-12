package kiwiapollo.cobblemontrainerbattle.history;

import net.minecraft.nbt.NbtCompound;

import java.time.Instant;

public class TrainerRecord {
    private Instant timestamp;
    private int victory;
    private int defeat;
    private int kill;

    public TrainerRecord() {
        this.timestamp = Instant.EPOCH;
        this.victory = 0;
        this.defeat = 0;
        this.kill = 0;
    }

    public int getVictoryCount() {
        return victory;
    }

    public void setVictoryCount(int count) {
        victory = count;
        updateTimestamp();
    }

    public int getDefeatCount() {
        return defeat;
    }

    public void setDefeatCount(int count) {
        defeat = count;
        updateTimestamp();
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getKillCount() {
        return kill;
    }

    public void setKillCount(int count) {
        kill = count;
    }

    public void readFromNbt(NbtCompound nbt) {
        timestamp = Instant.ofEpochMilli(nbt.getLong("timestamp"));
        victory = nbt.getInt("victory");
        defeat = nbt.getInt("defeat");
        kill = nbt.getInt("kill");
    }

    public NbtCompound writeToNbt(NbtCompound nbt) {
        nbt.putLong("timestamp", timestamp.toEpochMilli());
        nbt.putInt("victory", victory);
        nbt.putInt("defeat", defeat);
        nbt.putInt("kill", kill);

        return nbt;
    }

    private void updateTimestamp() {
        timestamp = Instant.now();
    }
}
