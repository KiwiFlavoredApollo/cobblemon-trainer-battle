package kiwiapollo.trainerbattle.common;

import com.cobblemon.mod.common.api.abilities.Abilities;
import com.cobblemon.mod.common.api.moves.Moves;
import com.cobblemon.mod.common.api.pokemon.Natures;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import kiwiapollo.trainerbattle.TrainerBattle;
import kiwiapollo.trainerbattle.exceptions.GenderNotExistException;
import kiwiapollo.trainerbattle.exceptions.InvalidPokemonStatsException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public class Trainer {
    private final String name;
    private final JsonObject json;

    public Trainer(String name) {
        this.name = name;
        this.json = readJson();
    }

    private JsonObject readJson() {
        try {
            Path resourcePath = Paths.get("data/trainerbattle/radicalred").resolve(this.name);
            InputStream resourceInputStream = TrainerBattle.class.getResourceAsStream(resourcePath.toString());
            InputStreamReader reader = new InputStreamReader(resourceInputStream);

            BufferedReader bufferedReader = new BufferedReader(reader);
            JsonObject json = new Gson().fromJson(bufferedReader, JsonObject.class);
            bufferedReader.close();

            return json;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Pokemon> getPokemons() {
        List<JsonObject> pokemonJsonObjects = this.json.getAsJsonArray().asList().stream()
                .map(JsonElement::getAsJsonObject)
                .filter(this::isExistPokemon).toList();

        return pokemonJsonObjects.stream().map(this::createPokemon).toList();
    }

    private boolean isExistPokemon(JsonObject jsonObject) {
        Identifier identifier = Identifier.of("cobblemon", jsonObject.get("species").getAsString());
        return PokemonSpecies.INSTANCE.getByIdentifier(identifier) != null;
    }

    private Pokemon createPokemon(JsonObject jsonObject) {
        Identifier identifier = Identifier.of("cobblemon", jsonObject.get("species").getAsString());
        Pokemon pokemon = PokemonSpecies.INSTANCE.getByIdentifier(identifier).create(30);

        if (jsonObject.get("evs") != null && !jsonObject.get("evs").isJsonNull()) {
            setPokemonStats(pokemon::setEV, jsonObject.get("evs").getAsJsonObject());
        }

        if (jsonObject.get("ivs") != null && !jsonObject.get("ivs").isJsonNull()) {
            setPokemonStats(pokemon::setIV, jsonObject.get("ivs").getAsJsonObject());
        }

        if (jsonObject.get("nature") != null && !jsonObject.get("nature").isJsonNull()) {
            setPokemonNature(pokemon, jsonObject.get("nature").getAsJsonObject());
        }

        if (jsonObject.get("gender") != null && !jsonObject.get("gender").isJsonNull()) {
            setPokemonGender(pokemon, jsonObject.get("gender").getAsJsonObject());
        }

        if (jsonObject.get("moves") != null && !jsonObject.get("moves").isJsonNull()) {
            pokemon.getMoveSet().clear();
            jsonObject.get("moves").getAsJsonArray().forEach(move -> {
                pokemon.getMoveSet().add(Moves.INSTANCE.getByNameOrDummy(move.getAsString()).create());
            });
        }

        if (jsonObject.get("item") != null && !jsonObject.get("item").isJsonNull()) {
            String itemName = jsonObject.get("item").getAsString().toLowerCase().replace(" ", "_");
            Item item = Registries.ITEM.get(Identifier.of("cobblemon", itemName));

            pokemon.swapHeldItem(new ItemStack(item), false);
        }

        if (jsonObject.get("ability") != null && !jsonObject.get("ability").isJsonNull()) {
            String abilityName = jsonObject.get("ability").getAsString();
            pokemon.updateAbility(Abilities.INSTANCE.getOrException(abilityName).create(false));
        }

        return pokemon;
    }

    private void setPokemonStats(BiConsumer<Stats, Integer> consumer, JsonObject stats) {
        try {
            assertValidStats(stats);

            consumer.accept(Stats.HP, stats.get("hp").getAsInt());
            consumer.accept(Stats.ATTACK, stats.get("atk").getAsInt());
            consumer.accept(Stats.DEFENCE, stats.get("def").getAsInt());
            consumer.accept(Stats.SPECIAL_ATTACK, stats.get("spa").getAsInt());
            consumer.accept(Stats.SPECIAL_DEFENCE, stats.get("spd").getAsInt());
            consumer.accept(Stats.SPEED, stats.get("spe").getAsInt());

        } catch (InvalidPokemonStatsException ignored) {

        }
    }

    private void assertValidStats(JsonObject stats) throws InvalidPokemonStatsException {
        try {
            List<String> statNames = List.of("hp", "atk", "def", "spa", "spd", "spe");

            boolean isNull = statNames.stream()
                    .map(stats::get)
                    .anyMatch(Objects::isNull);

            boolean isJsonNull = statNames.stream()
                    .map(stats::get)
                    .anyMatch(JsonElement::isJsonNull);

            if (isNull || isJsonNull) {
                throw new InvalidPokemonStatsException();
            }

            statNames.forEach(name -> stats.get(name).getAsInt());

        } catch (NullPointerException | UnsupportedOperationException e) {
            throw new InvalidPokemonStatsException();
        }
    }

    private void setPokemonNature(Pokemon pokemon, JsonObject nature) {
        String name = nature.get("nature").getAsString().toLowerCase();
        Identifier identifier = Identifier.of("cobblemon", name);

        if (Natures.INSTANCE.all().contains(Natures.INSTANCE.getNature(identifier))) {
            pokemon.setNature(Natures.INSTANCE.getNature(identifier));
        }
    }

    private void setPokemonGender(Pokemon pokemon, JsonObject gender) {
        try {
            pokemon.setGender(parseGender(gender.get("gender").getAsString()));

        } catch (GenderNotExistException ignored) {

        }
    }

    Gender parseGender(String gender) throws GenderNotExistException {
        return switch (gender) {
            case "M" -> Gender.MALE;
            case "F" -> Gender.FEMALE;
            case "" -> Gender.GENDERLESS;
            default -> throw new GenderNotExistException();
        };
    }
}
