package kiwiapollo.cobblemontrainerbattle.command.executor;

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

            if (hasEmptyParty(player)) {
                player.sendMessage(getEmptyPartyErrorMessage());
                return 0;
            }

            File exportPath = getExportPath(player.getServer());

            if (!exportPath.exists()) {
                exportPath.mkdirs();
            }

            writeJsonFile(player);

            player.sendMessage(Text.translatable("command.cobblemontrainerbattle.success.export", player.getGameProfile().getName()));
            CobblemonTrainerBattle.LOGGER.error("Exported Pokémon : {}", player.getGameProfile().getName());

            return Command.SINGLE_SUCCESS;

        } catch (CommandSyntaxException | IOException e) {
            CobblemonTrainerBattle.LOGGER.error("Error occurred while exporting trainer file");
            return 0;
        }
    }

    // TODO
    private Text getEmptyPartyErrorMessage() {
        return Text.translatable("");
    }

    private boolean hasEmptyParty(ServerPlayerEntity player) {
        return Cobblemon.INSTANCE.getStorage().getParty(player).occupied() == 0;
    }

    private File getExportPath(MinecraftServer server) {
        final String exportDir = "export";

        File worldDir = server.getSavePath(WorldSavePath.ROOT).toFile();
        String exportPath = CobblemonTrainerBattle.MOD_ID + "/" + exportDir;

        return new File(worldDir, exportPath);
    }

    private void writeJsonFile(ServerPlayerEntity player) throws IOException {
        try (FileWriter fileWriter = new FileWriter(getExportFile(player))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(new Gson().toJsonTree(getShowdownPokemon(player)), fileWriter);
        }
    }

    private List<ShowdownPokemon> getShowdownPokemon(ServerPlayerEntity player) {
        List<Pokemon> pokemon = com.cobblemon.mod.common.Cobblemon.INSTANCE.getStorage()
                .getParty(player).toGappyList().stream()
                .filter(Objects::nonNull).toList();

        return pokemon.stream().map(new CobblemonPokemonParser()::toShowdownPokemon).toList();
    }

    private File getExportFile(ServerPlayerEntity player) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        String timestamp = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        String exportFileName = String.format("%s_%s.json", player.getGameProfile().getName().toLowerCase(), timestamp);
        return new File(getExportPath(player.getServer()), exportFileName);
    }
}
