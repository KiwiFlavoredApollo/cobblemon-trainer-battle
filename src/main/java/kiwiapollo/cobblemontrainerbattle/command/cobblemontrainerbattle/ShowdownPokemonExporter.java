package kiwiapollo.cobblemontrainerbattle.command.cobblemontrainerbattle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.*;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.pokemon.CobblemonPokemonParser;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemon;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class ShowdownPokemonExporter implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");

            if (!isPlayerPokemonExist(player)) {
                player.sendMessage(getNoPlayerPokemonErrorMessage());
                return 0;
            }

            File directory = getExportDirectory(player.getServer());
            String filename = getExportFilename(player);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            List<ShowdownPokemon> pokemon = getShowdownPokemon(player);
            File file = new File(directory, filename);

            try (FileWriter fileWriter = new FileWriter(file)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(new Gson().toJsonTree(pokemon), fileWriter);
            }

            player.sendMessage(getPlayerPokemonExportSuccessMessage(player));
            CobblemonTrainerBattle.LOGGER.error("Exported Pokémon : {}", player.getGameProfile().getName());

            return Command.SINGLE_SUCCESS;

        } catch (CommandSyntaxException | IOException e) {
            CobblemonTrainerBattle.LOGGER.error("Error occurred while exporting trainer file");
            return 0;
        }
    }

    private boolean isPlayerPokemonExist(ServerPlayerEntity player) {
        return Cobblemon.INSTANCE.getStorage().getParty(player).occupied() == 0;
    }

    private Text getNoPlayerPokemonErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.cobblemontrainerbattle.export.failed.no_player_pokemon").formatted(Formatting.RED);
    }

    private Text getPlayerPokemonExportSuccessMessage(ServerPlayerEntity player) {
        return Text.translatable("commands.cobblemontrainerbattle.cobblemontrainerbattle.export.success", player.getGameProfile().getName());
    }

    private File getExportDirectory(MinecraftServer server) {
        final String exportDir = "export";

        File worldDir = server.getSavePath(WorldSavePath.ROOT).toFile();
        String exportPath = CobblemonTrainerBattle.MOD_ID + "/" + exportDir;

        return new File(worldDir, exportPath);
    }

    private List<ShowdownPokemon> getShowdownPokemon(ServerPlayerEntity player) {
        List<Pokemon> pokemon = com.cobblemon.mod.common.Cobblemon.INSTANCE.getStorage()
                .getParty(player).toGappyList().stream()
                .filter(Objects::nonNull).toList();

        return pokemon.stream().map(new CobblemonPokemonParser()::toShowdownPokemon).toList();
    }

    private String getExportFilename(ServerPlayerEntity player) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        String timestamp = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        return String.format("%s_%s.json", player.getGameProfile().getName().toLowerCase(), timestamp);
    }
}
