package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import com.cobblemon.mod.common.pokemon.Pokemon;

import java.util.List;

public class Trainer {
    public String name;
    public List<Pokemon> pokemons;

    public Trainer(String name, List<Pokemon> pokemons) {
        this.name = name;
        this.pokemons = pokemons;
    }
}
