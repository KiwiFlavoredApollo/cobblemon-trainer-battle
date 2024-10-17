package kiwiapollo.cobblemontrainerbattle.parsers;

import com.cobblemon.mod.common.api.abilities.Abilities;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.Moves;
import com.cobblemon.mod.common.api.pokemon.Natures;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exceptions.PokemonParseException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public class SmogonPokemonParser {
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

    private final int pivotLevel;

    public SmogonPokemonParser(int pivotLevel) {
        this.pivotLevel = pivotLevel;
    }

    public Pokemon toCobblemonPokemon(SmogonPokemon smogonPokemon) throws PokemonParseException {
        Pokemon pokemon = createBasePokemon(smogonPokemon);

        setPokemonStats(pokemon::setEV, smogonPokemon.evs);
        setPokemonStats(pokemon::setIV, smogonPokemon.ivs);
        setPokemonGender(pokemon, smogonPokemon.gender);
        setPokemonMoveSet(pokemon, smogonPokemon.moves);
        setPokemonHeldItem(pokemon, smogonPokemon.item);
        setPokemonAbility(pokemon, smogonPokemon.ability);
        setPokemonLevel(pokemon, smogonPokemon.level);
        setPokemonNature(pokemon, smogonPokemon.nature);

        return pokemon;
    }

    private Pokemon createBasePokemon(SmogonPokemon smogonPokemon) throws PokemonParseException {
        try {
            Identifier identifier = toSpeciesIdentifier(smogonPokemon.species);
            return PokemonSpecies.INSTANCE.getByIdentifier(identifier).create(DEFAULT_LEVEL);

        } catch (NullPointerException e) {
            throw new PokemonParseException();
        }
    }

    private Identifier toSpeciesIdentifier(String species) throws PokemonParseException {
        try {
            boolean isNamespaceProvided = species.contains(":");
            if (isNamespaceProvided) {
                return Objects.requireNonNull(Identifier.tryParse(species));
            } else {
                return Objects.requireNonNull(Identifier.of("cobblemon", species));
            }
        } catch (NullPointerException e) {
            throw new PokemonParseException();
        }
    }

    private void setPokemonLevel(Pokemon pokemon, int level) {
        if (level >= RELATIVE_LEVEL_THRESHOLD) {
            pokemon.setLevel(level);

        } else {
            pokemon.setLevel(pivotLevel + level);
        }
    }

    private void setPokemonAbility(Pokemon pokemon, String ability) {
        try {
            pokemon.updateAbility(Abilities.INSTANCE.getOrException(
                    ability.replace(" ", "").toLowerCase()).create(false));

        } catch (NullPointerException | IllegalArgumentException ignored) {

        }
    }

    private void setPokemonStats(BiConsumer<Stats, Integer> consumer, Map<String, Integer> stats) {
        if (Objects.isNull(stats)) {
            return;
        }

        if (stats.containsKey("hp")) {
            consumer.accept(Stats.HP, stats.get("hp"));
        }

        if (stats.containsKey("atk")) {
            consumer.accept(Stats.ATTACK, stats.get("atk"));
        }

        if (stats.containsKey("def")) {
            consumer.accept(Stats.DEFENCE, stats.get("def"));
        }

        if (stats.containsKey("spa")) {
            consumer.accept(Stats.SPECIAL_ATTACK, stats.get("spa"));
        }

        if (stats.containsKey("spd")) {
            consumer.accept(Stats.SPECIAL_DEFENCE, stats.get("spd"));
        }

        if (stats.containsKey("spe")) {
            consumer.accept(Stats.SPEED, stats.get("spe"));
        }
    }

    private void setPokemonNature(Pokemon pokemon, String nature) {
        try {
            Objects.requireNonNull(nature);

            boolean isNamespaceProvided = nature.contains(":");
            if (isNamespaceProvided) {
                pokemon.setNature(Objects.requireNonNull(Natures.INSTANCE.getNature(nature)));

            } else {
                Identifier identifier = Identifier.of("cobblemon", nature);
                pokemon.setNature(Objects.requireNonNull(Natures.INSTANCE.getNature(identifier)));
            }

        } catch (NullPointerException ignored) {

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

    private void setPokemonHeldItem(Pokemon pokemon, String item) {
        try {
            Objects.requireNonNull(item);

            boolean isNamespaceProvided = item.contains(":");
            if (isNamespaceProvided) {
                Item itemToHold = Registries.ITEM.get(Identifier.tryParse(item));
                pokemon.swapHeldItem(new ItemStack(itemToHold), false);

            } else {
                Item itemToHold = Registries.ITEM.get(Identifier.of("cobblemon", item));
                pokemon.swapHeldItem(new ItemStack(itemToHold), false);
            }

        } catch (NullPointerException ignored) {

        }
    }

    private void setPokemonMoveSet(Pokemon pokemon, List<String> moveSet) {
        pokemon.getMoveSet().clear();
        for (String moveName : moveSet) {
            try {
                Move move = Moves.INSTANCE.getByName(toCobblemonMoveName(moveName)).create();
                pokemon.getMoveSet().add(move);

            } catch (NullPointerException e) {
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

    private String toCobblemonMoveName(String moveName) {
        if (EXCEPTIONAL_MOVE_NAMES.containsKey(moveName)) {
            return EXCEPTIONAL_MOVE_NAMES.get(moveName);
        }

        return normalizeMoveName(moveName);
    }
}
