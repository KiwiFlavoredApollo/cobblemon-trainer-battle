package kiwiapollo.cobblemontrainerbattle.parsers;

import com.google.gson.*;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SmogonPokemonExporter {
    private static final File GLOBAL_GAME_DIR = FabricLoader.getInstance().getGameDir().toFile();
    private static final File GAME_DIR = new File(GLOBAL_GAME_DIR, CobblemonTrainerBattle.NAMESPACE);
    public static final File EXPORT_DIR = new File(GAME_DIR, "exports");

    public SmogonPokemonExporter() {
        if (!EXPORT_DIR.exists()) {
            EXPORT_DIR.mkdirs();
        }
    }

    public void toJson(List<SmogonPokemon> smogonPokemons, File exportFile) throws IOException {
        try (FileWriter fileWriter = new FileWriter(exportFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(new Gson().toJsonTree(smogonPokemons), fileWriter);
        }
    }
}
