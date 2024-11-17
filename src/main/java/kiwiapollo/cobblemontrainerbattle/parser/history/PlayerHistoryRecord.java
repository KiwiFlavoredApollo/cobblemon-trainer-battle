package kiwiapollo.cobblemontrainerbattle.parser.history;

import net.minecraft.nbt.NbtCompound;

public interface PlayerHistoryRecord {
    void updateTimestamp();
    NbtCompound writeToNbt(NbtCompound nbt);
    PlayerHistoryRecord readFromNbt(NbtCompound nbt);
}
