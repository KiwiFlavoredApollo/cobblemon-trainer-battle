package kiwiapollo.cobblemontrainerbattle.global.history;

import net.minecraft.nbt.NbtCompound;

import java.time.Instant;

public class TrainerRecord implements NbtConvertible, BattleRecord, EntityRecord {
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
    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public int getKillCount() {
        return kill;
    }

    @Override
    public void setKillCount(int count) {
        kill = count;
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        timestamp = Instant.ofEpochMilli(nbt.getLong("timestamp"));
        victory = nbt.getInt("victory");
        defeat = nbt.getInt("defeat");
        kill = nbt.getInt("kill");
    }

    @Override
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
