package kiwiapollo.cobblemontrainerbattle.parser;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.abilities.Abilities;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.Moves;
import com.cobblemon.mod.common.api.pokemon.Natures;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.FormData;
import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.exception.PokemonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.function.BiConsumer;

public class ShowdownPokemonParser {
    public static final int DEFAULT_LEVEL = 50;
    public static final int MAXIMUM_RELATIVE_LEVEL = 7;
    public static final int MINIMUM_RELATIVE_LEVEL = -7;

    private final ServerPlayerEntity player;

    public ShowdownPokemonParser(ServerPlayerEntity player) {
        this.player = player;
    }

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
            Identifier identifier = toSpeciesResourceIdentifier(showdownPokemon.species);
            return PokemonSpecies.INSTANCE.getByIdentifier(identifier).create(DEFAULT_LEVEL);

        } catch (NullPointerException e) {
            throw new PokemonParseException();
        }
    }

    private void setPokemonForm(Pokemon pokemon, String form) {
        pokemon.setForm(toFormData(pokemon, form));
    }

    private FormData toFormData(Pokemon pokemon, String form) {
        try {
            return pokemon.getSpecies().getForms().stream()
                    .filter(formData -> formData.getName().equals(form)).toList().get(0);

        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return pokemon.getSpecies().getStandardForm();
        }
    }

    private String getFormName(String species, String form) {
        boolean isSpeciesContainForm = FormAspectProvider.FORM_ASPECTS.keySet().stream().anyMatch(species::contains);
        if (isSpeciesContainForm) {
            return FormAspectProvider.FORM_ASPECTS.keySet().stream().filter(species::contains).findFirst().get();
        } else {
            return form;
        }
    }

    private void setPokemonShiny(Pokemon pokemon, boolean shiny) {
        pokemon.setShiny(shiny);
    }

    private void setPokemonLevel(Pokemon pokemon, int level) throws PokemonParseException {
        try {
            boolean isRelativeLevel = MINIMUM_RELATIVE_LEVEL <= level && level <= MAXIMUM_RELATIVE_LEVEL;

            if (isRelativeLevel) {
                int maximumPartyLevel = Cobblemon.INSTANCE.getStorage().getParty(player).toGappyList().stream()
                        .filter(Objects::nonNull)
                        .map(Pokemon::getLevel)
                        .max(Comparator.naturalOrder()).get();

                pokemon.setLevel(maximumPartyLevel + level);

            } else if (level > MAXIMUM_RELATIVE_LEVEL) {
                pokemon.setLevel(level);

            } else {
                throw new PokemonParseException();
            }

        } catch (NullPointerException | NoSuchElementException ignored) {

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

            boolean isContainNamespace = nature.contains(":");
            if (isContainNamespace) {
                pokemon.setNature(Objects.requireNonNull(Natures.INSTANCE.getNature(new Identifier(nature.toLowerCase()))));

            } else {
                Identifier identifier = Identifier.of("cobblemon", nature.toLowerCase());
                pokemon.setNature(Objects.requireNonNull(Natures.INSTANCE.getNature(identifier)));
            }

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
        if (item.contains(":")) {
            return new ItemStack(Registries.ITEM.get(Identifier.tryParse(item)));

        } else {
            String path = item;
            path = path.toLowerCase();
            path = removeNonLowerCaseAlphanumeric(path);
            return new ItemStack(Registries.ITEM.get(Identifier.of("cobblemon", path)));
        }
    }

    private void setPokemonMoveSet(Pokemon pokemon, List<String> moveSet) {
        pokemon.getMoveSet().clear();
        for (String moveName : moveSet) {
            try {
                Move move = Moves.INSTANCE.getByName(new ShowdownMoveParser().toCobblemonMove(moveName)).create();
                pokemon.getMoveSet().add(move);

            } catch (NullPointerException e) {
                CobblemonTrainerBattle.LOGGER.error("Move not found: {}", moveName);
                CobblemonTrainerBattle.LOGGER.error("Please report this to mod author");
            }
        }
    }

    public static Identifier toSpeciesResourceIdentifier(String species) {
        if (species.contains(":")) {
            return Identifier.tryParse(species);
        } else {
            return Identifier.of("cobblemon", toSpeciesResourcePath(species));
        }
    }

    private static String toSpeciesResourcePath(String species) {
        String path = species;

        path = removeFormName(path);
        path = path.toLowerCase();
        path = removeNonLowerCaseAlphanumeric(path);

        return path;
    }

    private static String removeNonLowerCaseAlphanumeric(String species) {
        return species.replaceAll("[^a-z0-9]", "");
    }

    private static String removeFormName(String species) {
        String s = species;

        for (String form : FormAspectProvider.FORM_ASPECTS.keySet()) {
            s = s.replaceAll(form, "");
        }

        return s;
    }
}
