package kiwiapollo.cobblemontrainerbattle.battle;

import com.cobblemon.mod.common.battles.BattleFormat;
import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;

public class BattleFormatFactory implements SimpleFactory<BattleFormat> {
    private final String format;

    public BattleFormatFactory(String format) {
        this.format = format;
    }

    public BattleFormat create() {
        return BattleFormat.Companion.getGEN_9_SINGLES();
    }
}
