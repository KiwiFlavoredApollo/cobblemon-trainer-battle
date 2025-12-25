package kiwiapollo.cobblemontrainerbattle.command.exportpokemon;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.pokemon.CobblemonPokemonParser;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemon;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.WorldSavePath;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public abstract class ShowdownTeamExporter implements Command<ServerCommandSource> {
    private static final String SHOWDOWN_TEAM = "showdown_team";

    protected int run(ServerPlayerEntity player) {
        try {
            List<ShowdownPokemon> pokemon = getShowdownPokemon(player);
            File file = getShowdownTeamFile(player);

            writeShowdownTeamFile(pokemon, file);

            player.sendMessage(getExportPokemonSuccessMessage(player));
            CobblemonTrainerBattle.LOGGER.info("Created showdown team file: {}", file.getPath());

            return Command.SINGLE_SUCCESS;

        } catch (IOException e) {
            player.sendMessage(getExportPokemonErrorMessage(player));
            CobblemonTrainerBattle.LOGGER.error("Failed to create showdown team file: {}", player.getGameProfile().getName());
            return 0;
        }
    }

    private void writeShowdownTeamFile(List<ShowdownPokemon> pokemon, File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            Files.createDirectories(file.getParentFile().toPath());

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(gson.toJsonTree(pokemon), writer);
        }
    }

    private Text getExportPokemonSuccessMessage(ServerPlayerEntity player) {
        return Text.translatable("commands.cobblemontrainerbattle.exportpokemon.success", player.getGameProfile().getName());
    }

    private Text getExportPokemonErrorMessage(ServerPlayerEntity player) {
        return Text.translatable("commands.cobblemontrainerbattle.exportpokemon.error", player.getGameProfile().getName()).formatted(Formatting.RED);
    }

    private List<ShowdownPokemon> getShowdownPokemon(ServerPlayerEntity player) {
        List<Pokemon> pokemon = Cobblemon.INSTANCE.getStorage()
                .getParty(player).toGappyList().stream()
                .filter(Objects::nonNull).toList();

        return pokemon.stream().map(new CobblemonPokemonParser()::toShowdownPokemon).toList();
    }

    private File getShowdownTeamFile(ServerPlayerEntity player) {
        File directory = getShowdownTeamDirectory(player.getServer());
        String filename = getShowdownTeamFilename(player);

        return new File(directory, filename);
    }

    private File getShowdownTeamDirectory(MinecraftServer server) {
        File parent = server.getSavePath(WorldSavePath.ROOT).toFile();
        String child = CobblemonTrainerBattle.MOD_ID + "/" + SHOWDOWN_TEAM;

        return new File(parent, child);
    }

    private String getShowdownTeamFilename(ServerPlayerEntity player) {
        String name = player.getGameProfile().getName().toLowerCase();
        String timestamp = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        return String.format("%s_%s.json", name, timestamp);
    }

    protected ServerPlayerEntity getThisPlayer(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return context.getSource().getPlayerOrThrow();
    }

    protected ServerPlayerEntity getOtherPlayer(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return EntityArgumentType.getPlayer(context, "player");
    }

    public static class ThisPlayer extends ShowdownTeamExporter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            return super.run(getThisPlayer(context));
        }
    }

    public static class OtherPlayer extends ShowdownTeamExporter {
        @Override
        public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
            return super.run(getOtherPlayer(context));
        }
    }

    public static class Renamer implements ServerLifecycleEvents.ServerStarted {
        public static void initialize() {
            ServerLifecycleEvents.SERVER_STARTED.register(new ShowdownTeamExporter.Renamer());
        }

        @Override
        public void onServerStarted(MinecraftServer server) {
            File oldDirectory = getOldShowdownTeamDirectory(server);
            File newDirectory = getNewShowdownTeamDirectory(server);

            oldDirectory.renameTo(newDirectory);
        }

        private File getOldShowdownTeamDirectory(MinecraftServer server) {
            File parent = server.getSavePath(WorldSavePath.ROOT).toFile();
            String child = CobblemonTrainerBattle.MOD_ID + "/" + "export";

            return new File(parent, child);
        }

        private File getNewShowdownTeamDirectory(MinecraftServer server) {
            File parent = server.getSavePath(WorldSavePath.ROOT).toFile();
            String child = CobblemonTrainerBattle.MOD_ID + "/" + SHOWDOWN_TEAM;

            return new File(parent, child);
        }
    }
}
