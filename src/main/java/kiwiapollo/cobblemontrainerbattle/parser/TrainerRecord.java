package kiwiapollo.cobblemontrainerbattle.parser;

import java.time.Instant;

public class TrainerRecord {
    public Instant timestamp;
    public int victory;
    public int defeat;
    public int kill;

    public TrainerRecord(Instant timestamp, int victory, int defeat, int kill) {
        this.timestamp = timestamp;
        this.victory = victory;
        this.defeat = defeat;
        this.kill = kill;
    }

    public TrainerRecord() {
        this.timestamp = Instant.now();
        this.victory = 0;
        this.defeat = 0;
        this.kill = 0;
    }
}
