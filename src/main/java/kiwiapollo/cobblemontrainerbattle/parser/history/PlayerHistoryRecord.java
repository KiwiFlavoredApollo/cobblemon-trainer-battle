package kiwiapollo.cobblemontrainerbattle.parser.history;

import net.minecraft.nbt.NbtCompound;

public interface PlayerHistoryRecord {
    void readFromNbt(NbtCompound nbt);
    void writeToNbt(NbtCompound nbt);
}
