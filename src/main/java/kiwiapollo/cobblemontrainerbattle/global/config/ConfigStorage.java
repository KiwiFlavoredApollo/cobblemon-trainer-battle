package kiwiapollo.cobblemontrainerbattle.global.config;

public class ConfigStorage {
    private static ConfigStorage instance;

    private int maximumTrainerSpawnCount;
    private int trainerSpawnIntervalInSeconds;
    private boolean allowHostileTrainerSpawn;

    public static ConfigStorage getInstance() {
        if (instance == null) {
            instance = new ConfigStorage();
        }

        return instance;
    }

    private ConfigStorage() {

    }

    public void update(Config config) {
        this.maximumTrainerSpawnCount = config.maximumTrainerSpawnCount;
        this.trainerSpawnIntervalInSeconds = config.trainerSpawnIntervalInSeconds;
        this.allowHostileTrainerSpawn = config.allowHostileTrainerSpawn;
    }

    public int getMaximumTrainerSpawnCount() {
        return maximumTrainerSpawnCount;
    }

    public int getTrainerSpawnIntervalInSeconds() {
        return trainerSpawnIntervalInSeconds;
    }

    public boolean getAllowHostileTrainerSpawn() {
        return allowHostileTrainerSpawn;
    }
}
