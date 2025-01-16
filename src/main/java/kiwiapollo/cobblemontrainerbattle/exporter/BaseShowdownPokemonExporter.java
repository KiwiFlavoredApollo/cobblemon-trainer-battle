package kiwiapollo.cobblemontrainerbattle.exporter;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.*;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.pokemon.CobblemonPokemonParser;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemon;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class BaseShowdownPokemonExporter implements ShowdownPokemonExporter {
    private static final File GAME_DIR = FabricLoader.getInstance().getGameDir().toFile();
    public static final File EXPORT_DIR = new File(GAME_DIR, CobblemonTrainerBattle.MOD_ID);

    private final ServerPlayerEntity player;
    private final Function<Integer, Integer> toLevel;

    public BaseShowdownPokemonExporter(ServerPlayerEntity player, Function<Integer, Integer> toLevel) {
        this.player = player;
        this.toLevel = toLevel;
    }

    public void toJson() throws IOException {
        if (!EXPORT_DIR.exists()) {
            EXPORT_DIR.mkdirs();
        }

        writeJsonFile();
    }

    private void writeJsonFile() throws IOException {
        try (FileWriter fileWriter = new FileWriter(getExportFile())) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(new Gson().toJsonTree(getShowdownPokemon()), fileWriter);
        }
    }

    private List<ShowdownPokemon> getShowdownPokemon() {
        List<Pokemon> pokemons = com.cobblemon.mod.common.Cobblemon.INSTANCE.getStorage()
                .getParty(player).toGappyList().stream()
                .filter(Objects::nonNull).toList();

        List<ShowdownPokemon> showdownPokemons = pokemons.stream().map(new CobblemonPokemonParser()::toShowdownPokemon).toList();
        showdownPokemons.forEach(smogonPokemon -> smogonPokemon.level = toLevel.apply(smogonPokemon.level));
        return showdownPokemons;
    }

    private File getExportFile() {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        String timestamp = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        String exportFileName = String.format("%s_%s.json", player.getGameProfile().getName().toLowerCase(), timestamp);
        return new File(BaseShowdownPokemonExporter.EXPORT_DIR, exportFileName);
    }
}
