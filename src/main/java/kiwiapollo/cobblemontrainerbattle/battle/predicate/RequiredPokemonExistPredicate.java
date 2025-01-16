package kiwiapollo.cobblemontrainerbattle.battle.predicate;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.pokemon.ShowdownPokemon;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;

public class RequiredPokemonExistPredicate extends PokemonPredicate {
    private final List<ShowdownPokemon> required;
    private ShowdownPokemon error;

    public RequiredPokemonExistPredicate(List<ShowdownPokemon> required) {
        this.required = required.stream().filter(Objects::nonNull).toList();
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.required_pokemon_exist", toPokemonDescriptor(error));
    }

    @Override
    public boolean test(PlayerBattleParticipant player) {
        List<Pokemon> party = player.getParty().toGappyList().stream().filter(Objects::nonNull).toList();
        for (ShowdownPokemon r : required) {
            if (!containsPokemon(party, r)) {
                error = r;
                return false;
            }
        }
        return true;
    }
}
