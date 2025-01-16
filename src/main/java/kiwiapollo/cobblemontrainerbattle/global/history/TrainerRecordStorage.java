package kiwiapollo.cobblemontrainerbattle.global.history;

import java.util.UUID;

public interface TrainerRecordStorage {
    TrainerRecord getTrainerRecord(UUID uuid, String trainer);

    void setTrainerRecord(UUID uuid, String trainer, TrainerRecord record);
}
