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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
            gson.toJson(getPlayerPartyInShowdownJsonFormat(), fileWriter);

            CobblemonTrainerBattle.LOGGER.info(String.format("Exported: %s", player.getGameProfile().getName()));
            CobblemonTrainerBattle.LOGGER.info(getExportFile().getPath());

        } catch (ShowdownJsonExportException | IOException e) {
            CobblemonTrainerBattle.LOGGER.error("An error occurred while exporting Pokemon");
        }
    }

    private File getExportFile() {
        return new File(EXPORT_DIR, String.format("%s.json", player.getGameProfile().getName()));
    }

    private JsonArray getPlayerPartyInShowdownJsonFormat() throws ShowdownJsonExportException {
        List<Pokemon> pokemons = Cobblemon.INSTANCE.getStorage()
                .getParty(player).toGappyList().stream()
                .filter(Objects::nonNull).toList();
        List<JsonObject> cobblemon = pokemons.stream().map(pokemon -> pokemon.saveToJSON(new JsonObject())).toList();

        List<ShowdownPokemon> showdown = new ArrayList<>();
        for (JsonObject jsonObject : cobblemon) {
            showdown.add(toShowdownJsonObject(jsonObject));
        }

        return new Gson().toJsonTree(showdown).getAsJsonArray();
    }

    private ShowdownPokemon toShowdownJsonObject(JsonObject cobblemon) throws ShowdownJsonExportException {
        try {
            return new ShowdownPokemon(
                    getName(cobblemon),
                    getSpecies(cobblemon),
                    getItem(cobblemon),
                    getAbility(cobblemon),
                    getGender(cobblemon),
                    getNature(cobblemon),
                    getLevel(cobblemon),
                    getEvs(cobblemon),
                    getIvs(cobblemon),
                    getMoves(cobblemon)
            );

        } catch (IllegalStateException | UnsupportedOperationException | NullPointerException ignored) {
            throw new ShowdownJsonExportException();
        }
    }

    private String getName(JsonObject cobblemon) {
        try {
            return cobblemon.get("Nickname").getAsJsonObject().get("text").getAsString();

        } catch (IllegalStateException | UnsupportedOperationException | NullPointerException ignored) {
            return "";
        }
    }

    private String getSpecies(JsonObject cobblemon) {
        return cobblemon.get("Species").getAsString();
    }

    private String getItem(JsonObject cobblemon) {
        try {
            return cobblemon.get("HeldItem").getAsJsonObject().get("id").getAsString();

        } catch (IllegalStateException | UnsupportedOperationException | NullPointerException ignored) {
            return "None";
        }
    }

    private String getAbility(JsonObject cobblemon) {
        return cobblemon.get("Ability").getAsJsonObject().get("AbilityName").getAsString();
    }

    private String getGender(JsonObject cobblemon) {
        String gender = cobblemon.get("Gender").getAsString();
        return switch(gender) {
            case "MALE" -> "M";
            case "FEMALE" -> "F";
            case "GENDERLESS" -> "";
            default -> throw new IllegalStateException("Unexpected value: " + gender);
        };
    }

    private String getNature(JsonObject cobblemon) {
        return cobblemon.get("Nature").getAsString();
    }

    private int getLevel(JsonObject cobblemon) {
        return cobblemon.get("Level").getAsInt();
    }

    private Map<String, Integer> getEvs(JsonObject cobblemon) {
        if (cobblemon.has("EVs") && !cobblemon.get("EVs").isJsonNull()) {
            return toShowdownPokemonStats(cobblemon.get("EVs").getAsJsonObject());
        } else {
            return Map.of();
        }
    }

    private Map<String, Integer> getIvs(JsonObject cobblemon) {
        if (cobblemon.has("IVs") && !cobblemon.get("IVs").isJsonNull()) {
            return toShowdownPokemonStats(cobblemon.get("IVs").getAsJsonObject());
        } else {
            return Map.of();
        }
    }

    private Map<String, Integer> toShowdownPokemonStats(JsonObject cobblemonStats) {
        return Map.of(
                "hp", cobblemonStats.has("hp") ? cobblemonStats.get("hp").getAsInt() : 0,
                "atk", cobblemonStats.has("attack") ? cobblemonStats.get("attack").getAsInt() : 0,
                "def", cobblemonStats.has("defence") ? cobblemonStats.get("defence").getAsInt() : 0,
                "spa", cobblemonStats.has("special_attack") ? cobblemonStats.get("special_attack").getAsInt() : 0,
                "spd", cobblemonStats.has("special_defence") ? cobblemonStats.get("special_defence").getAsInt() : 0,
                "spe", cobblemonStats.has("speed") ? cobblemonStats.get("speed").getAsInt() : 0
        );
    }

    private List<String> getMoves(JsonObject cobblemon) {
        List<String> moveSetKeys = List.of(
                "MoveSet0",
                "MoveSet1",
                "MoveSet2",
                "MoveSet3"
        );

        JsonObject cobblemonMoveSet = cobblemon.get("MoveSet").getAsJsonObject();
        List<String> moveSet = new ArrayList<>();
        for (String key : moveSetKeys) {
            if (!cobblemonMoveSet.has(key)) {
                continue;
            }

            JsonObject move = cobblemonMoveSet.get(key).getAsJsonObject();
            moveSet.add(move.get("MoveName").getAsString());
        }

        return moveSet;
    }
}
