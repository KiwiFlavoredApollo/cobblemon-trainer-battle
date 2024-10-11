package kiwiapollo.cobblemontrainerbattle.groupbattle;

import net.minecraft.util.Identifier;

import java.time.Instant;
import java.util.UUID;

public class GroupBattleSession {
    public final UUID uuid;
    public final Identifier trainerGroupIdentifier;

    public int defeatedTrainerCount;
    public Instant timestamp;
    public boolean isDefeated;

    public GroupBattleSession(Identifier trainerGroupIdentifier) {
        this.uuid = UUID.randomUUID();
        this.trainerGroupIdentifier = trainerGroupIdentifier;

        this.defeatedTrainerCount = 0;
        this.timestamp = Instant.now();
        this.isDefeated = false;
    }
}
