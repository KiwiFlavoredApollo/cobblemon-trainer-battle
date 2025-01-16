package kiwiapollo.cobblemontrainerbattle.global.history;

import net.minecraft.nbt.NbtCompound;

public interface NbtConvertible {
    void readFromNbt(NbtCompound nbt);
    NbtCompound writeToNbt(NbtCompound nbt);
}
