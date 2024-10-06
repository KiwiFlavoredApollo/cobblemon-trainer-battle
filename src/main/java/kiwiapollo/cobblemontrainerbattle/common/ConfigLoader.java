package kiwiapollo.cobblemontrainerbattle.common;

import com.google.gson.Gson;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exceptions.LoadingConfigFailedException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ConfigLoader {
    private static final File CONFIG_DIR =
            new File(FabricLoader.getInstance().getConfigDir().toFile(), CobblemonTrainerBattle.NAMESPACE);
    private static final File CONFIG_FILE = new File(CONFIG_DIR, "config.json");
    private static final Gson GSON = new Gson();

    public static Config load() {
        try {
            Config config = loadExistingConfig();
            assertSuccessLoadingConfig(config);
            return config;

        } catch (LoadingConfigFailedException e) {
            copyDefaultConfig();
            return loadDefaultConfig();
        }
    }

    private static void copyDefaultConfig() {
        try (InputStream defaults = ConfigLoader.class.getClassLoader()
                .getResourceAsStream("config/defaults.json")) {

            if (!CONFIG_DIR.exists()) {
                CONFIG_DIR.mkdirs();
            }

            Files.copy(defaults, CONFIG_FILE.toPath(), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Config loadExistingConfig() throws LoadingConfigFailedException {
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            return GSON.fromJson(reader, Config.class);
        } catch (IOException e) {
            throw new LoadingConfigFailedException();
        }
    }

    private static Config loadDefaultConfig() {
        try (InputStream defaults = ConfigLoader.class.getClassLoader()
                .getResourceAsStream("config/defaults.json");
             InputStreamReader reader = new InputStreamReader(defaults)) {

            return GSON.fromJson(reader, Config.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void assertSuccessLoadingConfig(Config config) throws LoadingConfigFailedException {
        if (config == null) {
           throw new LoadingConfigFailedException();
        }
    }
}
