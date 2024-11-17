package kiwiapollo.cobblemontrainerbattle.parser.history;

import net.minecraft.nbt.NbtCompound;

public interface PlayerHistoryRecord {
    NbtCompound writeToNbt(NbtCompound nbt);
    PlayerHistoryRecord readFromNbt(NbtCompound nbt);
}
