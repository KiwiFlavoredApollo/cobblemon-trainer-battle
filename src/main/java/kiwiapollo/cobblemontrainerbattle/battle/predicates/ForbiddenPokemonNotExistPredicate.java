package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.parser.ShowdownPokemon;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;

public class ForbiddenPokemonNotExistPredicate extends PokemonPredicate {
    private final List<ShowdownPokemon> forbidden;
    private ShowdownPokemon error;

    public ForbiddenPokemonNotExistPredicate(List<ShowdownPokemon> forbidden) {
        this.forbidden = forbidden.stream().filter(Objects::nonNull).toList();
    }

    @Override
    public MutableText getErrorMessage() {
        return Text.translatable("predicate.cobblemontrainerbattle.error.required_pokemon_exist", toPokemonDescriptor(error));
    }

    @Override
    public boolean test(PlayerBattleParticipant player) {
        List<Pokemon> party = player.getParty().toGappyList().stream().filter(Objects::nonNull).toList();
        for (ShowdownPokemon f : forbidden) {
            if (containsPokemon(party, f)) {
                error = f;
                return false;
            }
        }
        return true;
    }
}
