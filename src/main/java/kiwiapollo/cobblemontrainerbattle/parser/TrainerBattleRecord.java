package kiwiapollo.cobblemontrainerbattle.parser;

import java.time.Instant;

public class TrainerBattleRecord {
    public Instant lastBattleTimestamp;
    public int victoryCount;
    public int defeatCount;

    public TrainerBattleRecord(Instant lastBattleTimestamp, int victoryCount, int defeatCount) {
        this.lastBattleTimestamp = lastBattleTimestamp;
        this.victoryCount = victoryCount;
        this.defeatCount = defeatCount;
    }

    public TrainerBattleRecord() {
        this.lastBattleTimestamp = Instant.now();
        this.victoryCount = 0;
        this.defeatCount = 0;
    }
}
