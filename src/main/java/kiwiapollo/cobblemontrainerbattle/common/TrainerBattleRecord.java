package kiwiapollo.cobblemontrainerbattle.common;

import java.time.Instant;

public class TrainerBattleRecord {
    public Instant lastBattleDate;
    public int victoryCount;
    public int defeatCount;

    public TrainerBattleRecord(Instant lastBattleDate, int victoryCount, int defeatCount) {
        this.lastBattleDate = lastBattleDate;
        this.victoryCount = victoryCount;
        this.defeatCount = defeatCount;
    }

    public TrainerBattleRecord() {
        this.lastBattleDate = Instant.now();
        this.victoryCount = 0;
        this.defeatCount = 0;
    }
}
