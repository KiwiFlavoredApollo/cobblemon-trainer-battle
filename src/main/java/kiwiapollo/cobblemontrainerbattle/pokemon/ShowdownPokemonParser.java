package kiwiapollo.cobblemontrainerbattle.pokemon;

import com.cobblemon.mod.common.api.abilities.Abilities;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.Moves;
import com.cobblemon.mod.common.api.pokemon.Natures;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exception.PokemonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.function.BiConsumer;

public class ShowdownPokemonParser {
    public static final int DEFAULT_LEVEL = 50;

    public Pokemon toCobblemonPokemon(ShowdownPokemon showdownPokemon) throws PokemonParseException {
        Pokemon pokemon = createBasePokemon(showdownPokemon);

        setPokemonForm(pokemon, getFormName(showdownPokemon.species, showdownPokemon.form));
        setPokemonShiny(pokemon, showdownPokemon.shiny);
        setPokemonStats(pokemon::setEV, showdownPokemon.evs);
        setPokemonStats(pokemon::setIV, showdownPokemon.ivs);
        setPokemonGender(pokemon, showdownPokemon.gender);
        setPokemonMoveSet(pokemon, showdownPokemon.moves);
        setPokemonHeldItem(pokemon, showdownPokemon.item);
        setPokemonAbility(pokemon, showdownPokemon.ability);
        setPokemonLevel(pokemon, showdownPokemon.level);
        setPokemonNature(pokemon, showdownPokemon.nature);

        return pokemon;
    }

    private Pokemon createBasePokemon(ShowdownPokemon showdownPokemon) throws PokemonParseException {
        try {
            Identifier identifier = toCobblemonDefaultedIdentifier(removeFormName(showdownPokemon.species));
            return PokemonSpecies.INSTANCE.getByIdentifier(identifier).create(DEFAULT_LEVEL);

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
            String id = ability;
            id = normalize(id);
            id = sanitize(id);
            pokemon.updateAbility(Abilities.INSTANCE.getOrException(id).create(false));

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
            Identifier identifier = toCobblemonDefaultedIdentifier(nature);
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
        Identifier identifier = toCobblemonDefaultedIdentifier(item);
        return new ItemStack(Registries.ITEM.get(identifier));
    }

    private void setPokemonMoveSet(Pokemon pokemon, List<String> moveSet) {
        if (isUseDefaultMoveSet(moveSet)) {
            return;
        }

        pokemon.getMoveSet().clear();
        for (String moveName : moveSet) {
            try {
                Move move = Moves.INSTANCE.getByName(new ShowdownMoveParser().toCobblemonMove(moveName)).create();
                pokemon.getMoveSet().add(move);

            } catch (NullPointerException e) {
                List<String> ignore = List.of("None");

                if (ignore.contains(moveName)) {
                    return;
                }

                CobblemonTrainerBattle.LOGGER.error("Move not found: {}", moveName);
            }
        }
    }

    private boolean isUseDefaultMoveSet(List<String> moveSet) {
        return moveSet == null || moveSet.isEmpty();
    }

    public static Identifier toCobblemonDefaultedIdentifier(String string) {
        if (string.contains(String.valueOf(Identifier.NAMESPACE_SEPARATOR))) {
            String id = string;
            id = normalize(id);
            id = sanitize(id);
            return Identifier.tryParse(id);

        } else {
            String path = string;
            path = normalize(path);
            path = sanitize(path);
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

    private static String sanitize(String string) {
        String s = string;

        s = s.replaceAll("[^a-z0-9]", "");

        return s;
    }

    private static String normalize(String string) {
        String s = string;

        s = s.toLowerCase();

        return s;
    }
}
