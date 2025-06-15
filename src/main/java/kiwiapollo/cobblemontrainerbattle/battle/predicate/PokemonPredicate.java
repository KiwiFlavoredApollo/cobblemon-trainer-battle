package kiwiapollo.cobblemontrainerbattle.battle.predicate;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemon;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemonParser;
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
            Identifier r = ShowdownPokemonParser.toSpecies(required).getResourceIdentifier();
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
            if (pokemon.form == null) {
                Species species = ShowdownPokemonParser.toSpecies(pokemon);
                return species.getTranslatedName();

            } else {
                Species species = ShowdownPokemonParser.toSpecies(pokemon);
                return species.getTranslatedName().append(" ").append(pokemon.form);
            }

        } catch (NullPointerException e) {
            Species species = ShowdownPokemonParser.toSpecies(pokemon);
            CobblemonTrainerBattle.LOGGER.error("Unknown Pokemon species: {}", species.getResourceIdentifier());
            throw new IllegalStateException(e);
        }
    }
}
