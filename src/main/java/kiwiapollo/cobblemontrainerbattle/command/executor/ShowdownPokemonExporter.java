package kiwiapollo.cobblemontrainerbattle.command.executor;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.*;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.NormalLevelPlayer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.predicate.MessagePredicate;
import kiwiapollo.cobblemontrainerbattle.battle.predicate.PlayerPartyNotEmptyPredicate;
import kiwiapollo.cobblemontrainerbattle.pokemon.CobblemonPokemonParser;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemon;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
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
    private static final String EXPORT_DIR = "export";

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");

            MessagePredicate<PlayerBattleParticipant> predicate = new PlayerPartyNotEmptyPredicate();
            if (!predicate.test(new NormalLevelPlayer(player))) {
                player.sendMessage(predicate.getErrorMessage());
                return 0;
            }

            File worldDir = player.getServer().getSavePath(WorldSavePath.ROOT).toFile();
            File exportPath = new File(worldDir, CobblemonTrainerBattle.MOD_ID + "/" + EXPORT_DIR);

            if (!exportPath.exists()) {
                exportPath.mkdirs();
            }

            writeJsonFile(player);

            return Command.SINGLE_SUCCESS;

        } catch (CommandSyntaxException | IOException e) {
            CobblemonTrainerBattle.LOGGER.error("Error occurred while exporting trainer file");
            return 0;
        }
    }

    private void writeJsonFile(ServerPlayerEntity player) throws IOException {
        try (FileWriter fileWriter = new FileWriter(getExportFile(player))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(new Gson().toJsonTree(getShowdownPokemon(player)), fileWriter);
        }
    }

    private List<ShowdownPokemon> getShowdownPokemon(ServerPlayerEntity player) {
        List<Pokemon> pokemons = com.cobblemon.mod.common.Cobblemon.INSTANCE.getStorage()
                .getParty(player).toGappyList().stream()
                .filter(Objects::nonNull).toList();

        return pokemons.stream().map(new CobblemonPokemonParser()::toShowdownPokemon).toList();
    }

    private File getExportFile(ServerPlayerEntity player) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        String timestamp = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        String exportFileName = String.format("%s_%s.json", player.getGameProfile().getName().toLowerCase(), timestamp);
        return new File(ShowdownPokemonExporter.EXPORT_DIR, exportFileName);
    }
}
