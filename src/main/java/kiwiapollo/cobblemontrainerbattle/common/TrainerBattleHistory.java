package kiwiapollo.cobblemontrainerbattle.common;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.time.Instant;
import java.util.*;

public class TrainerBattleHistory {

    private final Map<Identifier, TrainerBattleRecord> recordRegistry;

    public TrainerBattleHistory(ServerPlayerEntity player) {
        recordRegistry = new HashMap<>();
    }

    public void addPlayerVictory(Identifier trainer) {
        TrainerBattleRecord record = getTrainerBattleRecord(trainer);

        record.victoryCount += 1;
        record.lastBattleDate = Instant.now();

        recordRegistry.put(trainer, record);
    }

    public void addPlayerDefeat(Identifier trainer) {
        TrainerBattleRecord record = getTrainerBattleRecord(trainer);

        record.defeatCount += 1;
        record.lastBattleDate = Instant.now();

        recordRegistry.put(trainer, record);
    }

    private TrainerBattleRecord getTrainerBattleRecord(Identifier trainer) {
        if (recordRegistry.containsKey(trainer)) {
            return recordRegistry.get(trainer);
        } else {
            return new TrainerBattleRecord();
        }
    }

    public void remove(Identifier trainer) {
        recordRegistry.remove(trainer);
    }

    public boolean isTrainerDefeated(Identifier trainer) {
        if (!recordRegistry.containsKey(trainer)) {
            return false;
        } else {
            return recordRegistry.get(trainer).victoryCount > 0;
        }
    }
}
