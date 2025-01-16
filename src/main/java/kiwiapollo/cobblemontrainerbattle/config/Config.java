package kiwiapollo.cobblemontrainerbattle.config;

import com.google.gson.annotations.SerializedName;

public class Config {
    @SerializedName("maximum_trainer_spawn_count")
    public int maximumTrainerSpawnCount;

    @SerializedName("spawn_interval_in_seconds")
    public int spawnIntervalInSeconds;
}
