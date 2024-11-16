package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.parser.ShowdownPokemon;

import java.util.List;

public abstract class PokemonPredicate implements MessagePredicate<PlayerBattleParticipant> {
    protected boolean containsPokemon(List<Pokemon> party, ShowdownPokemon required) {
        for (Pokemon p : party) {
            boolean isSpeciesEqual = normalize(p.getSpecies().getName()).equals(normalize(required.species));
            boolean isFormEqual = p.getForm().getName().equals(required.form);

            if (isSpeciesEqual && isFormEqual) {
                return true;
            }
        }
        return false;
    }

    private String normalize(String species) {
        String normalized = species;
        normalized = normalized.replaceAll("^cobblemon:", "");
        normalized = normalized.replaceAll("[-\\s]", "");
        normalized = normalized.toLowerCase();
        return normalized;
    }

    private String toPascalCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String[] words = input.split("[^a-zA-Z0-9]+");
        StringBuilder pascalCased = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                pascalCased.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase());
            }
        }

        return pascalCased.toString();
    }

    protected String toPokemonDescriptor(ShowdownPokemon pokemon) {
        return String.format("%s %s", toPascalCase(normalize(pokemon.species)), pokemon.form);
    }
}
