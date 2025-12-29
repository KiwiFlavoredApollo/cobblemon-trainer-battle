package kiwiapollo.cobblemontrainerbattle.pokemon;

import com.cobblemon.mod.common.api.abilities.Abilities;
import com.cobblemon.mod.common.api.moves.Moves;
import com.cobblemon.mod.common.api.pokemon.Natures;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exception.PokemonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.function.BiConsumer;

public class ShowdownPokemonParser {
    private static final int DEFAULT_LEVEL = 50;

    public Pokemon toCobblemonPokemon(ShowdownPokemon showdown) throws PokemonParseException {
        Pokemon cobblemon = createPokemon(showdown.species);

        setPokemonForm(cobblemon, getFormName(showdown.species, showdown.form));
        setPokemonShiny(cobblemon, showdown.shiny);
        setPokemonStats(cobblemon::setEV, showdown.evs);
        setPokemonStats(cobblemon::setIV, showdown.ivs);
        setPokemonGender(cobblemon, showdown.gender);
        setPokemonMoveSet(cobblemon, showdown.moves);
        setPokemonHeldItem(cobblemon, showdown.item);
        setPokemonAbility(cobblemon, showdown.ability);
        setPokemonLevel(cobblemon, showdown.level);
        setPokemonNature(cobblemon, showdown.nature);
        setPokemonUncatchable(cobblemon);

        return cobblemon;
    }

    private Pokemon createPokemon(String species) throws PokemonParseException {
        try {
            return ShowdownPokemonParser.toSpecies(species).create(DEFAULT_LEVEL);

        } catch (ClassCastException | NullPointerException e) {
            throw new PokemonParseException();
        }
    }

    private void setPokemonForm(Pokemon pokemon, String form) {
        pokemon.getSpecies().getForms().stream()
                .filter(formData -> formData.getName().equals(form)).findFirst()
                .ifPresent(pokemon::setForm);
    }

    private String getFormName(String species, String form) {
        return FormAspectProvider.FORM_ASPECTS.keySet().stream().filter(species::contains).findFirst().orElse(form);
    }

    private void setPokemonShiny(Pokemon pokemon, boolean shiny) {
        pokemon.setShiny(shiny);
    }

    protected void setPokemonLevel(Pokemon pokemon, int level) {
        pokemon.setLevel(level);
    }

    private void setPokemonAbility(Pokemon pokemon, String ability) {
        try {
            pokemon.updateAbility(Abilities.INSTANCE.getOrException(toAbilityName(ability)).create(false));

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
            Identifier identifier = toNatureIdentifier(Objects.requireNonNull(nature));
            pokemon.setNature(Objects.requireNonNull(Natures.INSTANCE.getNature(identifier)));

        } catch (NullPointerException ignored) {

        }
    }

    private void setPokemonGender(Pokemon pokemon, String gender) {
        try {
            pokemon.setGender(toGender(gender));
        } catch (NullPointerException | IllegalArgumentException ignored) {

        }
    }

    private Gender toGender(String gender) throws IllegalArgumentException {
        return switch (gender) {
            case "M" -> Gender.MALE;
            case "F" -> Gender.FEMALE;
            case "N" -> Gender.GENDERLESS;
            default -> throw new IllegalArgumentException();
        };
    }

    private void setPokemonHeldItem(Pokemon pokemon, String item) {
        try {
            pokemon.swapHeldItem(toHeldItem(item), false);
        } catch (NullPointerException ignored) {
            
        }
    }

    private ItemStack toHeldItem(String item) {
        Identifier identifier = toHeldItemIdentifier(item);
        return new ItemStack(Registries.ITEM.get(identifier));
    }

    private void setPokemonMoveSet(Pokemon pokemon, List<String> moves) {
        List<String> filtered = moves.stream().filter(move -> !new ShowdownMoveParser().shouldIgnore(move)).toList();

        if (shouldUseDefaultMoveSet(filtered)) {
            return;
        }

        pokemon.getMoveSet().clear();
        for (String move : filtered) {
            try {
                pokemon.getMoveSet().add(Moves.INSTANCE.getByName(new ShowdownMoveParser().toCobblemonMove(move)).create());

            } catch (NullPointerException e) {
                CobblemonTrainerBattle.LOGGER.error("Move not found: {}", move);
            }
        }
    }

    private void setPokemonUncatchable(Pokemon pokemon) {
        PokemonProperties.Companion.parse("uncatchable=yes").apply(pokemon);
    }

    private boolean shouldUseDefaultMoveSet(List<String> moves) {
        return moves == null || moves.isEmpty();
    }

    public static Species toSpecies(String species) {
        if (isRandomSpecies(species)) {
            return PokemonSpecies.INSTANCE.random();

        } else {
            String string = removeFormName(species);
            Identifier identifier = toSpeciesIdentifier(string);
            return PokemonSpecies.INSTANCE.getByIdentifier(identifier);
        }
    }

    private static boolean isRandomSpecies(String species) {
        return species.equals(Identifier.of(CobblemonTrainerBattle.MOD_ID, "random").toString());
    }

    private static Identifier toSpeciesIdentifier(String string) {
        if (string.contains(String.valueOf(Identifier.NAMESPACE_SEPARATOR))) {
            return Identifier.tryParse(string);

        } else {
            String path = string;

            path = path.toLowerCase();
            path = path.replaceAll("[^a-z0-9_:]", "");

            return Identifier.of("cobblemon", path);
        }
    }

    private static Identifier toNatureIdentifier(String string) {
        if (string.contains(String.valueOf(Identifier.NAMESPACE_SEPARATOR))) {
            return Identifier.tryParse(string);

        } else {
            String path = string;

            path = path.toLowerCase();
            path = path.replaceAll("[^a-z0-9_:]", "");

            return Identifier.of("cobblemon", path);
        }
    }

    private static Identifier toHeldItemIdentifier(String string) {
        if (string.contains(String.valueOf(Identifier.NAMESPACE_SEPARATOR))) {
            return Identifier.tryParse(string);

        } else {
            String path = string;

            path = path.toLowerCase();
            path = path.replace(" ", "_");
            path = path.replaceAll("[^a-z0-9_:]", "");

            return Identifier.of("cobblemon", path);
        }
    }

    private static String removeFormName(String species) {
        String s = species;

        for (String form : FormAspectProvider.FORM_ASPECTS.keySet()) {
            s = s.replaceAll(form, "");
        }

        return s;
    }

    private static String toAbilityName(String ability) {
        String name = ability;

        name = name.toLowerCase();
        name = name.replaceAll("[^a-z0-9_:]", "");

        return name;
    }
}
