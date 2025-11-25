package kiwiapollo.cobblemontrainerbattle.template;

import com.cobblemon.mod.common.pokemon.Pokemon;

public class PokemonLevelPair {
    private final Pokemon pokemon;
    private final int level;

    public PokemonLevelPair(Pokemon pokemon, int level) {
        this.pokemon = pokemon;
        this.level = level;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public int getLevel() {
        return level;
    }
}
