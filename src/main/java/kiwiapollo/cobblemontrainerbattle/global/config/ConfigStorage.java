package kiwiapollo.cobblemontrainerbattle.global.config;

public class ConfigStorage {
    private static ConfigStorage instance;

    private int maximumTrainerSpawnCount;
    private int trainerSpawnIntervalInSeconds;

    public static ConfigStorage getInstance() {
        if (instance == null) {
            instance = new ConfigStorage();
        }

        return instance;
    }

    public void update(Config config) {
        this.maximumTrainerSpawnCount = config.maximumTrainerSpawnCount;
        this.trainerSpawnIntervalInSeconds = config.trainerSpawnIntervalInSeconds;
    }

    public int getMaximumTrainerSpawnCount() {
        return maximumTrainerSpawnCount;
    }

    public int getTrainerSpawnIntervalInSeconds() {
        return trainerSpawnIntervalInSeconds;
    }
}
