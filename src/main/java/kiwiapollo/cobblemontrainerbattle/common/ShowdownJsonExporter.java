package kiwiapollo.cobblemontrainerbattle.common;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.*;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exceptions.ShowdownJsonExportException;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ShowdownJsonExporter {
    private static final File GAME_DIR =
            new File(FabricLoader.getInstance().getGameDir().toFile(), CobblemonTrainerBattle.NAMESPACE);
    public static final File EXPORT_DIR = new File(GAME_DIR, "exports");

    private final ServerPlayerEntity player;

    public ShowdownJsonExporter(ServerPlayerEntity player) {
        this.player = player;

        if (!EXPORT_DIR.exists()) {
            EXPORT_DIR.mkdirs();
        }
    }

    public void export() {
        try (FileWriter fileWriter = new FileWriter(getExportFile())) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(playerPartyToJsonArray(), fileWriter);

            CobblemonTrainerBattle.LOGGER.info(String.format("Exported: %s", player.getGameProfile().getName()));
            CobblemonTrainerBattle.LOGGER.info(getExportFile().getPath());

        } catch (IOException e) {
            CobblemonTrainerBattle.LOGGER.error("An error occurred while exporting Pokemon");
        }
    }

    private JsonArray playerPartyToJsonArray() {
        List<Pokemon> pokemonList = Cobblemon.INSTANCE.getStorage()
                .getParty(player).toGappyList().stream()
                .filter(Objects::nonNull).toList();

        JsonArray pokemonJsonArray = new JsonArray();
        for (Pokemon pokemon : pokemonList) {
            pokemonJsonArray.add(pokemon.saveToJSON(new JsonObject()));
        }

        return pokemonJsonArray;
    }

    private File getExportFile() {
        return new File(EXPORT_DIR, String.format("%s.json", player.getGameProfile().getName()));
    }

    private JsonObject toShowdownJsonFormat(JsonObject cobblemon) throws ShowdownJsonExportException {
        JsonObject showdown = new JsonObject();

        showdown.addProperty("name", toShowdownName(cobblemon));
        showdown.addProperty("species", toShowdownSpecies(cobblemon));
        showdown.addProperty("item", toShowdownItem(cobblemon));
        showdown.addProperty("ability", toShowdownAbility(cobblemon));
        showdown.addProperty("gender", toShowdownGender(cobblemon));
        showdown.addProperty("nature", toShowdownNature(cobblemon));
        showdown.addProperty("level", toShowdownLevel(cobblemon));
        showdown.add("evs", toShowdownEvs(cobblemon));
        showdown.add("ivs", toShowdownIvs(cobblemon));
        showdown.add("moves", toShowdownMoveSet(cobblemon));

        return showdown;
    }

    private String toShowdownName(JsonObject cobblemon) {
        try {
            return cobblemon.get("Name").getAsString();

        } catch (UnsupportedOperationException | NullPointerException ignored) {
            return "";
        }
    }

    private String toShowdownSpecies(JsonObject cobblemon) throws ShowdownJsonExportException {
        try {
            return cobblemon.get("Species").getAsString();

        } catch (UnsupportedOperationException | NullPointerException ignored) {
            throw new ShowdownJsonExportException();
        }
    }

    private String toShowdownItem(JsonObject cobblemon) throws ShowdownJsonExportException {
        try {
            return cobblemon.get("Item").getAsString();

        } catch (UnsupportedOperationException | NullPointerException ignored) {
            throw new ShowdownJsonExportException();
        }
    }

    private String toShowdownAbility(JsonObject cobblemon) throws ShowdownJsonExportException {
        try {
            return cobblemon.get("Ability").getAsJsonObject().get("AbilityName").getAsString();

        } catch (IllegalStateException | UnsupportedOperationException | NullPointerException ignored) {
            throw new ShowdownJsonExportException();
        }
    }

    private String toShowdownGender(JsonObject cobblemon) throws ShowdownJsonExportException {
        try {
            String gender = cobblemon.get("Gender").getAsString();
            return switch(gender) {
                case "MALE" -> "M";
                case "FEMALE" -> "F";
                case "GENDERLESS" -> "";
                default -> throw new ShowdownJsonExportException();
            };

        } catch (UnsupportedOperationException | NullPointerException ignored) {
            throw new ShowdownJsonExportException();
        }
    }

    private String toShowdownNature(JsonObject cobblemon) throws ShowdownJsonExportException {
        try {
            return cobblemon.get("Nature").getAsString();

        } catch (UnsupportedOperationException | NullPointerException ignored) {
            throw new ShowdownJsonExportException();
        }
    }

    private int toShowdownLevel(JsonObject cobblemon) throws ShowdownJsonExportException {
        try {
            return cobblemon.get("Level").getAsInt();

        } catch (NullPointerException | UnsupportedOperationException e) {
            throw new ShowdownJsonExportException();
        }
    }

    private JsonObject toShowdownEvs(JsonObject cobblemon) throws ShowdownJsonExportException {
        try {
            return toShowdownPokemonStats(cobblemon.get("EVs").getAsJsonObject());

        } catch (UnsupportedOperationException | NullPointerException ignored) {
            throw new ShowdownJsonExportException();
        }
    }

    private JsonObject toShowdownIvs(JsonObject cobblemon) throws ShowdownJsonExportException {
        try {
            return toShowdownPokemonStats(cobblemon.get("IVs").getAsJsonObject());

        } catch (UnsupportedOperationException | NullPointerException ignored) {
            throw new ShowdownJsonExportException();
        }
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

    private JsonArray toShowdownMoveSet(JsonObject cobblemon) throws ShowdownJsonExportException {
        try {
            JsonObject cobblemonMoveSet = cobblemon.get("MoveSet").getAsJsonObject();

            List<String> keys = List.of(
                    "MoveSet0",
                    "MoveSet1",
                    "MoveSet2",
                    "MoveSet3"
            );

            JsonArray moveSet = new JsonArray();
            for (String key : keys) {
                try {
                    JsonObject move = cobblemonMoveSet.get(key).getAsJsonObject();
                    moveSet.add(move.get("MoveName").getAsString());

                } catch (IllegalStateException | NullPointerException ignored) {

                }
            }

            return moveSet;

        } catch (IllegalStateException | UnsupportedOperationException | NullPointerException ignored) {
            throw new ShowdownJsonExportException();
        }
    }
}
