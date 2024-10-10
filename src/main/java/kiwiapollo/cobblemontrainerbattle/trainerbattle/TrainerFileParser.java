package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.abilities.Abilities;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.Moves;
import com.cobblemon.mod.common.api.pokemon.Natures;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.Nature;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exceptions.InvalidPokemonStatsException;
import kiwiapollo.cobblemontrainerbattle.exceptions.CobblemonMoveNameNotExistException;
import kiwiapollo.cobblemontrainerbattle.exceptions.PokemonParseException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.util.*;
import java.util.function.BiConsumer;

public class TrainerFileParser {
    public static final int DEFAULT_LEVEL = 50;
    public static final int RELATIVE_LEVEL_THRESHOLD = 10;
    public static final Map<String, String> EXCEPTIONAL_MOVE_NAMES = Map.of(
            "Drain Kiss", "drainingkiss",
            "Bad Tantrum", "stompingtantrum",
            "FirstImpress", "firstimpression",
            "Dark Hole", "darkvoid",
            "Para Charge", "paraboliccharge",
            "HiHorsepower", "highhorsepower",
            "Expand Force", "expandingforce",
            "Aqua Fang", "aquajet",
            "Teary Look", "tearfullook"
    );

    private final ServerPlayerEntity player;

    public TrainerFileParser(ServerPlayerEntity player) {
        this.player = player;
    }

    public List<Pokemon> parse(TrainerFile trainerFile) {
        List<Pokemon> pokemons = new ArrayList<>();
        for (JsonElement pokemon: trainerFile.pokemons.asList()) {
            try {
                pokemons.add(toPokemon(pokemon.getAsJsonObject()));

            } catch (PokemonParseException | IllegalStateException e) {
                CobblemonTrainerBattle.LOGGER.warn(String.format("An error occurred while parsing Pokemon"));
            }
        }
        return pokemons;
    }

    private Pokemon toPokemon(JsonObject jsonObject) throws PokemonParseException {
        if (!jsonObject.has("species") || jsonObject.get("species").isJsonNull()) {
            throw new PokemonParseException();
        }

        Identifier identifier = createSpeciesIdentifier(jsonObject);
        Pokemon pokemon = PokemonSpecies.INSTANCE.getByIdentifier(identifier).create(DEFAULT_LEVEL);

        if (jsonObject.get("evs") != null && !jsonObject.get("evs").isJsonNull()) {
            setPokemonStats(pokemon::setEV, jsonObject.get("evs").getAsJsonObject());
        }

        if (jsonObject.get("ivs") != null && !jsonObject.get("ivs").isJsonNull()) {
            setPokemonStats(pokemon::setIV, jsonObject.get("ivs").getAsJsonObject());
        }

        if (jsonObject.get("nature") != null && !jsonObject.get("nature").isJsonNull()) {
            setPokemonNature(pokemon, jsonObject.get("nature").getAsString());
        }

        if (jsonObject.get("gender") != null && !jsonObject.get("gender").isJsonNull()) {
            setPokemonGender(pokemon, jsonObject.get("gender").getAsString());
        }

        if (jsonObject.get("moves") != null && !jsonObject.get("moves").isJsonNull()) {
            setPokemonMoveSet(pokemon, jsonObject.get("moves").getAsJsonArray());
        }

        if (jsonObject.get("item") != null && !jsonObject.get("item").isJsonNull()) {
            setPokemonHeldItem(pokemon, jsonObject.get("item").getAsString());
        }

        if (jsonObject.get("ability") != null && !jsonObject.get("ability").isJsonNull()) {
            setPokemonAbility(pokemon, jsonObject.get("ability").getAsString());
        }

        if (jsonObject.get("level") != null && !jsonObject.get("level").isJsonNull()) {
            setPokemonLevel(pokemon, jsonObject.get("level").getAsInt());
        }

        return pokemon;
    }

    private Identifier createSpeciesIdentifier(JsonObject jsonObject) throws PokemonParseException {
        try {
            String species = jsonObject.get("species").getAsString();
            return new Identifier(species);

        } catch (InvalidIdentifierException e) {
            String species = jsonObject.get("species").getAsString();
            Identifier cobblemon = Identifier.of("cobblemon", species.toLowerCase());
            if (cobblemon != null) {
                return cobblemon;
            }

            throw new PokemonParseException();

        } catch (NullPointerException | UnsupportedOperationException e) {
            throw new PokemonParseException();
        }
    }

    private boolean isIdentifierString(String species) {
        List<String> split = Arrays.stream(species.split(":")).toList();
        return split.size() == 2 && !split.contains("");
    }

    private void setPokemonLevel(Pokemon pokemon, int level) {
        if (level >= RELATIVE_LEVEL_THRESHOLD) {
            pokemon.setLevel(level);

        } else {
            List<Pokemon> playerPokemons = Cobblemon.INSTANCE.getStorage().getParty(player).toGappyList();
            if (playerPokemons.stream().allMatch(Objects::isNull)) return;
            int playerMaximumLevel = playerPokemons.stream()
                    .filter(Objects::nonNull)
                    .map(Pokemon::getLevel)
                    .max(Comparator.naturalOrder()).get();
            pokemon.setLevel(playerMaximumLevel + level);
        }
    }

    private void setPokemonAbility(Pokemon pokemon, String ability) {
        try {
            pokemon.updateAbility(Abilities.INSTANCE.getOrException(
                    ability.replace(" ", "").toLowerCase()).create(false));

        } catch (IllegalArgumentException ignored) {

        }
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

    private void setPokemonNature(Pokemon pokemon, String natureName) {
        Nature nature = Natures.INSTANCE.getNature(createNatureIdentifier(natureName));

        if (Natures.INSTANCE.all().contains(nature)) {
            pokemon.setNature(nature);
        }
    }

    private Identifier createNatureIdentifier(String nature) {
        try {
            return new Identifier(nature);

        } catch (InvalidIdentifierException e) {
            return Identifier.of("cobblemon", nature.toLowerCase());
        }
    }

    private void setPokemonGender(Pokemon pokemon, String gender) {
        switch (gender) {
            case "M" -> pokemon.setGender(Gender.MALE);
            case "F" -> pokemon.setGender(Gender.FEMALE);
            case "" -> pokemon.setGender(Gender.GENDERLESS);
            default -> {}
        }
    }

    private void setPokemonHeldItem(Pokemon pokemon, String itemName) {
        Item item = Registries.ITEM.get(Identifier.of("cobblemon", itemName.replace(" ", "_").toLowerCase()));

        pokemon.swapHeldItem(new ItemStack(item), false);
    }

    private void setPokemonMoveSet(Pokemon pokemon, JsonArray moveSet) {
        pokemon.getMoveSet().clear();
        List<String> moveNames = moveSet.getAsJsonArray().asList().stream().map(JsonElement::getAsString).toList();
        for (String moveName : moveNames) {
            try {
                assertExistCobblmonMoveName(moveName);

                String cobblemonMoveName = toCobblemonMove(moveName);
                Move move = Moves.INSTANCE.getByNameOrDummy(cobblemonMoveName).create();
                pokemon.getMoveSet().add(move);

            } catch (CobblemonMoveNameNotExistException e) {
                CobblemonTrainerBattle.LOGGER.error(String.format("Move not found: %s", moveName));
                CobblemonTrainerBattle.LOGGER.error("Please report to mod author");
            }
        }
    }

    private String normalizeMoveName(String moveName) {
        return moveName.replace(" ", "")
                .replace("-", "")
                .toLowerCase();
    }

    private String toCobblemonMove(String moveName) {
        if (Moves.INSTANCE.names().contains(normalizeMoveName(moveName))) {
            return normalizeMoveName(moveName);
        }

        if (EXCEPTIONAL_MOVE_NAMES.containsKey(moveName)) {
            return EXCEPTIONAL_MOVE_NAMES.get(moveName);
        }

        throw new RuntimeException();
    }

    private void assertExistCobblmonMoveName(String moveName) throws CobblemonMoveNameNotExistException {
        if (moveName.equals("None")) {
            throw new CobblemonMoveNameNotExistException();
        }

        if (Moves.INSTANCE.names().contains(normalizeMoveName(moveName))) {
            return;
        }

        if (EXCEPTIONAL_MOVE_NAMES.containsKey(moveName)) {
            CobblemonTrainerBattle.LOGGER.warn(String.format("Exceptional move name: %s", moveName));
            return;
        }

        throw new CobblemonMoveNameNotExistException();
    }
}
