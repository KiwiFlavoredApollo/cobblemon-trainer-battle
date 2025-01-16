package kiwiapollo.cobblemontrainerbattle.global.history;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.io.File;

public class PlayerHistoryUtils {
    public static File getHistoryPath(MinecraftServer server) {
        final String historyDir = "history";

        File worldDir = server.getSavePath(WorldSavePath.ROOT).toFile();
        String historyPath = CobblemonTrainerBattle.MOD_ID + "/" + historyDir;

        return new File(worldDir, historyPath);
    }
}
