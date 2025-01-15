package kiwiapollo.cobblemontrainerbattle.parser.pokemon;

import com.cobblemon.mod.common.pokemon.Pokemon;

public class RelativeLevelShowdownPokemonParser extends ShowdownPokemonParser {
    private static final int PIVOT = 50;

    @Override
    protected void setPokemonLevel(Pokemon pokemon, int level) {
        pokemon.setLevel(PIVOT + level);
    }
}
