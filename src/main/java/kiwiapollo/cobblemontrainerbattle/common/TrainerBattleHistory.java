package kiwiapollo.cobblemontrainerbattle.common;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.WorldSavePath;

import java.io.File;
import java.io.IOException;

public class TrainerBattleHistory {
    public static File WORLD_DIR = MinecraftServer.getSavePath(WorldSavePath.ROOT).toFile();

    public static void save(ServerWorld world, NbtCompound data) {
        try {
            File worldDir = world.getServer().getSavePath(WorldSavePath.ROOT).toFile();
            File modDir = new File(worldDir, NAMESPACE);
            if (!modDir.exists()) {
                modDir.mkdirs();
            }

            File file = getFile(world);

            NbtIo.writeCompressed(data, file);

        } catch (IOException e) {

        }
    }

    public static NbtCompound load(ServerWorld world) {
        try {
            File file = getFile(world);
            if (!file.exists()) {
                return new NbtCompound();
            }

            return NbtIo.readCompressed(file);
        } catch (IOException e) {
            return new NbtCompound();
        }
    }

    private static File getFile(ServerWorld world) {
        File worldDir = world.getServer().getSavePath(WorldSavePath.ROOT).toFile();
        File modDir = new File(worldDir, NAMESPACE);
        return new File(worldDir, FILE_NAME);
    }
}
