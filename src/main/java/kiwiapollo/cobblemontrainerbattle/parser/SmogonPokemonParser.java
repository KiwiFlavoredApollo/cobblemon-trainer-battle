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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.*;
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
    public static final List<String> FORM_NAMES = List.of(
            "Alola",
            "Alola Bias",

            "Galar",
            "Galar Bias",

            "Hisui",
            "Hisui Bias",

            "Paldea-Aqua",
            "Paldea-Blaze",
            "Paldea-Combat",

            "Therian",

            "Zen",
            "Galar-Zen"
    );
    public static final Map<String, Set<String>> FORM_ASPECTS = Map.ofEntries(
            Map.entry("Alola", Set.of("alolan")),
            Map.entry("Alola Bias", Set.of("region-bias-alola")),

            Map.entry("Galar", Set.of("galarian")),
            Map.entry("Galar Bias", Set.of("region-bias-galar")),

            Map.entry("Hisui", Set.of("hisuian")),
            Map.entry("Hisui Bias", Set.of("region-bias-hisui")),

            Map.entry("Paldea", Set.of("paldean")),
            Map.entry("Paldea Bias", Set.of("region-bias-paldea")),

            Map.entry("Paldea-Aqua", Set.of("paldean-breed-aqua")),
            Map.entry("Paldea-Blaze", Set.of("paldean-breed-blaze")),
            Map.entry("Paldea-Combat", Set.of("paldean-breed-combat")),

            Map.entry("Therian", Set.of("therian")),

            Map.entry("Zen", Set.of("zen_mode")),
            Map.entry("Galar-Zen", Set.of("galarian", "zen_mode"))
    );

    private final ServerPlayerEntity player;

    public SmogonPokemonParser(ServerPlayerEntity player) {
        this.player = player;
    }

    public Pokemon toCobblemonPokemon(SmogonPokemon smogonPokemon) throws PokemonParseException {
        Pokemon pokemon = createBasePokemon(smogonPokemon);

        // setPokemonForm(pokemon, getFormName(smogonPokemon.species, smogonPokemon.form));
        // setPokemonShiny(pokemon, smogonPokemon.shiny);
        setPokemonStats(pokemon::setEV, smogonPokemon.evs);
        setPokemonStats(pokemon::setIV, smogonPokemon.ivs);
        // setPokemonGender(pokemon, smogonPokemon.gender);
        setPokemonMoveSet(pokemon, smogonPokemon.moves);
        setPokemonHeldItem(pokemon, smogonPokemon.item);
        setPokemonAbility(pokemon, smogonPokemon.ability);
        setPokemonLevel(pokemon, smogonPokemon.level);
        setPokemonNature(pokemon, smogonPokemon.nature);

        setPokemonAspects(pokemon, smogonPokemon);

        return pokemon;
    }

    private void setPokemonAspects(Pokemon pokemon, SmogonPokemon smogonPokemon) {
        Set<String> aspects = new HashSet<>();

        try {
            String form = getFormName(smogonPokemon.species, smogonPokemon.form);
            aspects.addAll(FORM_ASPECTS.get(form));

        } catch (NullPointerException ignored) {
            String form = getFormName(smogonPokemon.species, smogonPokemon.form);

            CobblemonTrainerBattle.LOGGER.error(String.format("Form not found: %s", form));
            CobblemonTrainerBattle.LOGGER.error("Please report to mod author");
        }

        try {
            Gender gender = toGender(smogonPokemon.gender);
            aspects.add(toGenderAspect(gender));
        } catch (IllegalArgumentException e) {
            Gender gender = pokemon.getGender();
            aspects.add(toGenderAspect(gender));
        }

        if (smogonPokemon.shiny) {
            aspects.add("shiny");
        }

        pokemon.setAspects(aspects);
    }

    private Pokemon createBasePokemon(SmogonPokemon smogonPokemon) throws PokemonParseException {
        try {
            Identifier identifier = toSpeciesIdentifier(smogonPokemon.species);
            return PokemonSpecies.INSTANCE.getByIdentifier(identifier).create(DEFAULT_LEVEL);

        } catch (NullPointerException e) {
            throw new PokemonParseException();
        }
    }

    private Identifier toSpeciesIdentifier(String species) throws NullPointerException {
        boolean isSpeciesContainNamespace = species.contains(":");
        boolean isSpeciesContainForm = FORM_NAMES.stream().anyMatch(species::contains);

        if (isSpeciesContainNamespace) {
            return Objects.requireNonNull(Identifier.tryParse(toLowerCaseNonAscii(species)));

        } else if (isSpeciesContainForm) {
            String cropped = species;

            for (String form : FORM_NAMES) {
                cropped = cropped.replaceAll(form, "");
            }
            cropped = cropped.replaceAll("-", "");
            cropped = toLowerCaseNonAscii(cropped);

            return Objects.requireNonNull(Identifier.of("cobblemon", cropped));

        } else {
            return Objects.requireNonNull(Identifier.of("cobblemon", toLowerCaseNonAscii(species)));
        }
    }

    private void setPokemonForm(Pokemon pokemon, String form) {
        // Hiroku said:
        // Nope aspects can't be set like that
        // aspects are calculated based off of data
        // You're better off waiting until 1.6,
        // there is a "forcedAspects" property you'll be able to use to do what you want

        // TODO I am not sure why this isn't working
        // Even if form is set, aspects get override by following methods
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
        boolean isSpeciesContainForm = FORM_NAMES.stream().anyMatch(species::contains);
        if (isSpeciesContainForm) {
            return FORM_NAMES.stream().filter(species::contains).findFirst().get();
        } else {
            return form;
        }
    }

    private String toLowerCaseNonAscii(String string) {
        String nonAscii = "[^\\x00-\\x7F]";
        return string.toLowerCase().replaceAll(nonAscii, "");
    }

    private void setPokemonShiny(Pokemon pokemon, boolean shiny) {
        pokemon.setShiny(shiny);
    }

    private void setPokemonLevel(Pokemon pokemon, int level) {
        try {
            if (level >= RELATIVE_LEVEL_THRESHOLD) {
                pokemon.setLevel(level);

            } else {
                int maximumPartyLevel = Cobblemon.INSTANCE.getStorage().getParty(player).toGappyList().stream()
                        .filter(Objects::nonNull)
                        .map(Pokemon::getLevel)
                        .max(Comparator.naturalOrder()).get();

                pokemon.setLevel(maximumPartyLevel + level);
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
        } catch (IllegalArgumentException ignored) {

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

    private String toGenderAspect(Gender gender) {
        return switch (gender) {
            case MALE -> "male";
            case FEMALE -> "female";
            case GENDERLESS -> "genderless";
        };
    }

    private void setPokemonHeldItem(Pokemon pokemon, String item) {
        try {
            Objects.requireNonNull(item);

            boolean isContainNamespace = item.contains(":");
            if (isContainNamespace) {
                Item itemToHold = Registries.ITEM.get(Identifier.tryParse(item.toLowerCase()));
                pokemon.swapHeldItem(new ItemStack(itemToHold), false);

            } else {
                Item itemToHold = Registries.ITEM.get(Identifier.of("cobblemon", item.toLowerCase()));
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
