package kiwiapollo.cobblemontrainerbattle.common;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CobblemonJsonObjectParser {
    public ShowdownPokemon toShowdownPokemon(JsonObject cobblemon) throws JsonParseException {
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
            throw new JsonParseException("");
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
