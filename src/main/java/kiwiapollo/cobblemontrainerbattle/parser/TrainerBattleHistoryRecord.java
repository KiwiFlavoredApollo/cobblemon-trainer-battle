package kiwiapollo.cobblemontrainerbattle.parser;

import java.time.Instant;

public class TrainerBattleHistoryRecord {
    public Instant timestamp;
    public int victory;
    public int defeat;

    public TrainerBattleHistoryRecord(Instant timestamp, int victory, int defeat) {
        this.timestamp = timestamp;
        this.victory = victory;
        this.defeat = defeat;
    }

    public TrainerBattleHistoryRecord() {
        this.timestamp = Instant.now();
        this.victory = 0;
        this.defeat = 0;
    }
}
