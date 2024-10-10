package kiwiapollo.cobblemontrainerbattle.common;

import com.google.gson.*;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exceptions.ShowdownPokemonExportException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ShowdownPokemonExporter {
    private static final File GAME_DIR =
            new File(FabricLoader.getInstance().getGameDir().toFile(), CobblemonTrainerBattle.NAMESPACE);
    public static final File EXPORT_DIR = new File(GAME_DIR, "exports");

    public ShowdownPokemonExporter() {
        if (!EXPORT_DIR.exists()) {
            EXPORT_DIR.mkdirs();
        }
    }

    public void toJson(List<ShowdownPokemon> showdownPokemons, File exportFile)
            throws ShowdownPokemonExportException {
        try (FileWriter fileWriter = new FileWriter(exportFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(new Gson().toJsonTree(showdownPokemons), fileWriter);

        } catch (IOException e) {
            throw new ShowdownPokemonExportException();
        }
    }
}
