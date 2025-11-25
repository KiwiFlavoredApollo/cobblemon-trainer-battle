package kiwiapollo.cobblemontrainerbattle.history;

import java.time.Instant;

public interface BattleRecord {
    int getVictoryCount();

    void setVictoryCount(int count);

    int getDefeatCount();

    void setDefeatCount(int count);

    Instant getTimestamp();
}
