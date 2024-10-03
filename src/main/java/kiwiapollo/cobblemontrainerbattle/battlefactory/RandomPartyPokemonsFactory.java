package kiwiapollo.cobblemontrainerbattle.battlefactory;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Pokemon;

import java.util.List;

public class RandomPartyPokemonsFactory {
    public List<Pokemon> create() {
        return List.of(
                PokemonSpecies.INSTANCE.random().create(BattleFactory.LEVEL),
                PokemonSpecies.INSTANCE.random().create(BattleFactory.LEVEL),
                PokemonSpecies.INSTANCE.random().create(BattleFactory.LEVEL)
        );
    }
}
