package kiwiapollo.cobblemontrainerbattle.global.history;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;

import java.io.File;

public class PlayerHistoryUtils {
    public static final String HISTORY_DIR = "history";

    public static File toHistoryPath(File worldDir) {
        return new File(worldDir, CobblemonTrainerBattle.MOD_ID + "/" + PlayerHistoryUtils.HISTORY_DIR);
    }
}
