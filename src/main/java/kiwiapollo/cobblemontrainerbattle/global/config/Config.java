package kiwiapollo.cobblemontrainerbattle.global.config;

import com.google.gson.annotations.SerializedName;

public class Config {
    @SerializedName("maximum_trainer_spawn_count")
    public int maximumTrainerSpawnCount = 1;

    @SerializedName("trainer_spawn_interval_in_seconds")
    public int trainerSpawnIntervalInSeconds = 60;

    @SerializedName("allow_hostile_trainer_spawn")
    public boolean allowHostileTrainerSpawn = true;
}
