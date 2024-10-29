package kiwiapollo.cobblemontrainerbattle.parser;

import com.google.gson.Gson;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ConfigLoader {
    private static final File GLOBAL_CONFIG_DIR = FabricLoader.getInstance().getConfigDir().toFile();
    private static final File CONFIG_DIR = new File(GLOBAL_CONFIG_DIR, CobblemonTrainerBattle.MOD_ID);
    private static final File CONFIG_FILE = new File(CONFIG_DIR, "config.json");

    public static Config load() {
        try {
            Config config = loadExistingConfig();
            assertSuccessLoadingConfig(config);
            return config;

        } catch (IOException | AssertionError e) {
            copyDefaultConfig();
            return loadDefaultConfig();
        }
    }

    private static void copyDefaultConfig() {
        try (InputStream defaults = getDefaultConfigResourceInputStream()) {
            if (!CONFIG_DIR.exists()) {
                CONFIG_DIR.mkdirs();
            }

            Files.copy(defaults, CONFIG_FILE.toPath(), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Config loadExistingConfig() throws IOException {
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            return new Gson().fromJson(reader, Config.class);
        }
    }

    private static Config loadDefaultConfig() {
        try (
                InputStream defaults = getDefaultConfigResourceInputStream();
                InputStreamReader reader = new InputStreamReader(defaults)
        ) {
            return new Gson().fromJson(reader, Config.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static InputStream getDefaultConfigResourceInputStream() {
        String defaultConfigResourcePath = "config/defaults.json";
        return ConfigLoader.class.getClassLoader().getResourceAsStream(defaultConfigResourcePath);
    }

    private static void assertSuccessLoadingConfig(Config config) throws AssertionError {
        if (config == null) {
           throw new AssertionError();
        }
    }
}
