package kiwiapollo.cobblemontrainerbattle.parser.pokemon;

import com.cobblemon.mod.common.pokemon.Pokemon;

public class RelativeLevelShowdownPokemonParser extends ShowdownPokemonParser {
    public static final int PIVOT = 50;

    @Override
    protected void setPokemonLevel(Pokemon pokemon, int level) {
        pokemon.setLevel(RelativeLevelShowdownPokemonParser.PIVOT + level);
    }
}
