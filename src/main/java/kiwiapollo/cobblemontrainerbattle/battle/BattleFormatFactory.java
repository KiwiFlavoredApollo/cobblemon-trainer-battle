package kiwiapollo.cobblemontrainerbattle.battle;

import com.cobblemon.mod.common.battles.BattleFormat;

public class BattleFormatFactory {
    private final String format;

    public BattleFormatFactory(String format) {
        this.format = format;
    }

    public BattleFormat create() {
        return BattleFormat.Companion.getGEN_9_SINGLES();
    }
}
