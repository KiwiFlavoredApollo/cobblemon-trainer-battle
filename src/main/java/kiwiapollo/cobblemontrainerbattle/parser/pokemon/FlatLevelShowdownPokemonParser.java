package kiwiapollo.cobblemontrainerbattle.parser.pokemon;

import com.cobblemon.mod.common.pokemon.Pokemon;

public class FlatLevelShowdownPokemonParser extends ShowdownPokemonParser {
    private static final int LEVEL = 50;

    @Override
    protected void setPokemonLevel(Pokemon pokemon, int level) {
        pokemon.setLevel(LEVEL);
    }
}
