package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.parser.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.parser.ShowdownPokemonParser;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public abstract class PokemonPredicate implements MessagePredicate<PlayerBattleParticipant> {
    protected boolean containsPokemon(List<Pokemon> party, ShowdownPokemon required) {
        for (Pokemon p : party) {
            if (isSpeciesEqual(p, required) && isFormEqual(p, required)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSpeciesEqual(Pokemon party, ShowdownPokemon required) {
        try {
            Identifier p = party.getSpecies().getResourceIdentifier();
            Identifier r = ShowdownPokemonParser.toSpeciesResourceIdentifier(required.species);
            return p.equals(r);

        } catch (NullPointerException e) {
            return false;
        }
    }

    private boolean isFormEqual(Pokemon party, ShowdownPokemon required) {
        if (required.form == null) {
            return true;
        } else {
            return party.getForm().getName().equals(required.form);
        }
    }

    protected Text toPokemonDescriptor(ShowdownPokemon pokemon) {
        try {
            Identifier identifier = ShowdownPokemonParser.toSpeciesResourceIdentifier(pokemon.species);
            Species species = PokemonSpecies.INSTANCE.getByIdentifier(identifier);

            boolean isFormExist = pokemon.form != null;

            if (isFormExist) {
                return species.getTranslatedName().append(" ").append(pokemon.form);

            } else {
                return species.getTranslatedName();
            }

        } catch (NullPointerException e) {
            Identifier identifier = ShowdownPokemonParser.toSpeciesResourceIdentifier(pokemon.species);
            CobblemonTrainerBattle.LOGGER.error("Unknown Pokemon species: {}", identifier);
            throw new IllegalStateException(e);
        }
    }
}
