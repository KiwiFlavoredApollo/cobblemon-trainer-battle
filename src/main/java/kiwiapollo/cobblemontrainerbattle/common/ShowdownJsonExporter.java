package kiwiapollo.cobblemontrainerbattle.common;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exceptions.EmptyPlayerPartyException;
import kotlinx.serialization.json.Json;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShowdownJsonExporter {
    private static final File CONFIG_DIR =
            new File(FabricLoader.getInstance().getConfigDir().toFile(), CobblemonTrainerBattle.NAMESPACE);
    public static final File EXPORT_DIR = new File(CONFIG_DIR, "exports");

    private final ServerPlayerEntity player;

    public ShowdownJsonExporter(ServerPlayerEntity player) {
        this.player = player;

        if (!EXPORT_DIR.exists()) {
            EXPORT_DIR.mkdirs();
        }
    }

    public void export() {
        try (FileWriter fileWriter = new FileWriter(getExportFile())) {
            assertNotEmptyPlayerParty();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(pokemonTeamToJsonArray(), fileWriter);

            CobblemonTrainerBattle.LOGGER.info(String.format("Exported: %s", player.getGameProfile().getName()));
            CobblemonTrainerBattle.LOGGER.info(getExportFile().getPath());

        } catch (EmptyPlayerPartyException e) {
            CobblemonTrainerBattle.LOGGER.error("Player has no Pokemon");

        } catch (IOException e) {
            CobblemonTrainerBattle.LOGGER.error("An error occurred while exporting Pokemon");
        }
    }

    private void assertNotEmptyPlayerParty() throws EmptyPlayerPartyException {
        List<Pokemon> pokemons = Cobblemon.INSTANCE.getStorage().getParty(player).toGappyList().stream()
                .filter(Objects::nonNull).toList();

        if (pokemons.isEmpty()) {
            throw new EmptyPlayerPartyException();
        }
    }

    private JsonArray pokemonTeamToJsonArray() {
        List<Pokemon> pokemons = Cobblemon.INSTANCE.getStorage().getParty(player).toGappyList().stream()
                .filter(Objects::nonNull).toList();

        JsonArray pokemonTeam = new JsonArray();
        for (Pokemon pokemon : pokemons) {
            pokemonTeam.add(pokemon.saveToJSON(new JsonObject()));
        }

        return pokemonTeam;
    }

    private File getExportFile() {
        return new File(EXPORT_DIR, String.format("%s.json", player.getGameProfile().getName()));
    }

    private JsonObject toShowdownJsonFormat(JsonObject cobblemon) {
        JsonObject showdown = new JsonObject();

        showdown.addProperty("name", "");
        showdown.addProperty("species", cobblemon.get("Species").toString());
        showdown.addProperty("item", cobblemon.get(""));
        showdown.addProperty("ability", cobblemon.get("Ability").getAsJsonObject().get("AbilityName").toString());
        showdown.addProperty("gender", cobblemon.get("Gender"));
        showdown.addProperty("nature", cobblemon.get("Nature").getAsString());
        showdown.add("evs", toShowdownPokemonStats(cobblemon.get("EVs").getAsJsonObject()));
        showdown.add("ivs", toShowdownPokemonStats(cobblemon.get("IVs").getAsJsonObject()));
        showdown.addProperty("level", cobblemon.get("Level").getAsInt());
        showdown.add("moves", toShowdownMoveSet(cobblemon.get("MoveSet").getAsJsonObject()));

        return showdown;
    }

    private String toShowdownGender(String gender) {
        return switch(gender) {
            case "Male" -> "M";
            case "Female" -> "F";
            case "GENDERLESS" -> "";
            default -> throw new IllegalStateException("Unexpected value: " + gender);
        };
    }

    private JsonObject toShowdownPokemonStats(JsonObject cobblemonStats) {
        JsonObject showdownStats = new JsonObject();

        showdownStats.addProperty("hp", cobblemonStats.get("hp").getAsInt());
        showdownStats.addProperty("atk", cobblemonStats.get("attack").getAsInt());
        showdownStats.addProperty("def", cobblemonStats.get("defence").getAsInt());
        showdownStats.addProperty("spa", cobblemonStats.get("special_attack").getAsInt());
        showdownStats.addProperty("spd", cobblemonStats.get("special_defence").getAsInt());
        showdownStats.addProperty("spe", cobblemonStats.get("speed").getAsInt());

        return showdownStats;
    }

    private JsonArray toShowdownMoveSet(JsonObject cobblemonMoveSet) {
        JsonArray moveSet = new JsonArray();

        List<String> keys = List.of(
                "MoveSet0",
                "MoveSet1",
                "MoveSet2",
                "MoveSet3"
        );

        for (String key : keys) {
            try {
                moveSet.add(cobblemonMoveSet.get(key).getAsJsonObject().get("MoveName").getAsString());

            } catch (IllegalStateException | NullPointerException ignored) {

            }
        }

        return moveSet;
    }
}
