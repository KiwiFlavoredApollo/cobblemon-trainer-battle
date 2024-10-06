package kiwiapollo.cobblemontrainerbattle.groupbattle;

import java.time.Instant;
import java.util.UUID;

public class GroupBattleSession {
    public final UUID uuid;
    public final String groupResourcePath;

    public int defeatedTrainerCount;
    public Instant timestamp;
    public boolean isDefeated;

    public GroupBattleSession(String groupResourcePath) {
        this.uuid = UUID.randomUUID();
        this.groupResourcePath = groupResourcePath;

        this.defeatedTrainerCount = 0;
        this.timestamp = Instant.now();
        this.isDefeated = false;
    }
}
