package kiwiapollo.cobblemontrainerbattle.global.config;

import com.google.gson.Gson;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ConfigLoader implements Command<ServerCommandSource> {
    private static final File CONFIG_DIR = new File(FabricLoader.getInstance().getConfigDir().toFile(), CobblemonTrainerBattle.MOD_ID);
    private static final File CONFIG_FILE = new File(CONFIG_DIR, "config.json");
    private static final Gson GSON = new Gson();

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            ConfigStorage.getInstance().update(load());

            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
            player.sendMessage(Text.translatable("command.cobblemontrainerbattle.success.reload"));
            CobblemonTrainerBattle.LOGGER.info("Loaded configuration");

            return Command.SINGLE_SUCCESS;

        } catch (Exception e) {
            return 0;
        }
    }

    public Config load() {
        try {
            return loadExistingConfig();

        } catch (IllegalStateException e) {
            CobblemonTrainerBattle.LOGGER.error("Failed to load config");

            copyDefaultConfig();

            return loadDefaultConfig();
        }
    }

    private void copyDefaultConfig() {
        try (InputStream defaults = getDefaultConfigInputStream()) {
            if (!CONFIG_DIR.exists()) {
                CONFIG_DIR.mkdirs();
            }

            Files.copy(defaults, CONFIG_FILE.toPath(), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Config loadExistingConfig() {
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            return GSON.fromJson(reader, Config.class);

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private Config loadDefaultConfig() {
        try (InputStreamReader reader = new InputStreamReader(getDefaultConfigInputStream())) {
            return GSON.fromJson(reader, Config.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private InputStream getDefaultConfigInputStream() {
        return ConfigLoader.class.getClassLoader().getResourceAsStream("config/defaults.json");
    }
}
