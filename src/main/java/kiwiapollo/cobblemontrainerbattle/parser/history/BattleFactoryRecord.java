package kiwiapollo.cobblemontrainerbattle.parser.history;

import net.minecraft.nbt.NbtCompound;

import java.time.Instant;

public class BattleFactoryRecord implements PlayerHistoryRecord, BattleRecord, MaximumStreakRecord {
    private Instant timestamp;
    private int streak;
    private int victory;
    private int defeat;

    public BattleFactoryRecord() {
        this.timestamp = Instant.now();
        this.streak = 0;
        this.victory = 0;
        this.defeat = 0;
    }

    @Override
    public int getVictoryCount() {
        return victory;
    }

    @Override
    public void setVictoryCount(int count) {
        victory = count;
    }

    @Override
    public int getDefeatCount() {
        return defeat;
    }

    @Override
    public void setDefeatCount(int count) {
        defeat = count;
    }

    @Override
    public int getMaximumStreak() {
        return streak;
    }

    @Override
    public void setMaximumStreak(int count) {
        streak = count;
    }

    @Override
    public void updateTimestamp() {
        timestamp = Instant.now();
    }

    @Override
    public NbtCompound writeToNbt(NbtCompound nbt) {
        nbt.putLong("timestamp", timestamp.toEpochMilli());
        nbt.putInt("streak", streak);
        nbt.putInt("victory", victory);
        nbt.putInt("defeat", defeat);

        return nbt;
    }

    @Override
    public BattleFactoryRecord readFromNbt(NbtCompound nbt) {
        timestamp = Instant.ofEpochMilli(nbt.getLong("timestamp"));
        streak = nbt.getInt("streak");
        victory = nbt.getInt("victory");
        defeat = nbt.getInt("defeat");

        return this;
    }
}
