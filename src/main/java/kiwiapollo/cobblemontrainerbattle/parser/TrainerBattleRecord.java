package kiwiapollo.cobblemontrainerbattle.parser;

import java.time.Instant;

public class TrainerBattleRecord {
    public Instant timestamp;
    public int victory;
    public int defeat;

    public TrainerBattleRecord(Instant timestamp, int victory, int defeat) {
        this.timestamp = timestamp;
        this.victory = victory;
        this.defeat = defeat;
    }

    public TrainerBattleRecord() {
        this.timestamp = Instant.now();
        this.victory = 0;
        this.defeat = 0;
    }
}
