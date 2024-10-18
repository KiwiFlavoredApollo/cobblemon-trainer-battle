package kiwiapollo.cobblemontrainerbattle.common;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;

import java.io.File;
import java.io.IOException;

public class NbtFileHandler {

    public static NbtCompound readNbtFromFile(File file) {
        try {
            return NbtIo.readCompressed(file);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to read NBT data from file.");
            return null;
        }
    }

    public static void writeNbtToFile(NbtCompound nbt, File file) {
        try {
            NbtIo.writeCompressed(nbt, file);
            System.out.println("NBT data successfully written to " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to write NBT data to file.");
        }
    }
}
