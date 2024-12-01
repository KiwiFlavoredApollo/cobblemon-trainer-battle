package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant;

import com.cobblemon.mod.common.battles.BattleFormat;

public class BattleFormatFactory {
    public BattleFormat create(String format) {
        return BattleFormat.Companion.getGEN_9_SINGLES();
    }
}
