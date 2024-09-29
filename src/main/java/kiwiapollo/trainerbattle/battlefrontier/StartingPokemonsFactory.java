package kiwiapollo.trainerbattle.battlefrontier;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Pokemon;

import java.util.List;

public class StartingPokemonsFactory {
    public List<Pokemon> create() {
        return List.of(
                PokemonSpecies.INSTANCE.random().create(100),
                PokemonSpecies.INSTANCE.random().create(100),
                PokemonSpecies.INSTANCE.random().create(100)
        );
    }
}
