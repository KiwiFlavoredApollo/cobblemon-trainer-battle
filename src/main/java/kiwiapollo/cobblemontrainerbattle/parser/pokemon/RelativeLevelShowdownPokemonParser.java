package kiwiapollo.cobblemontrainerbattle.parser.pokemon;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.common.RelativeLevelBattle;

public class RelativeLevelShowdownPokemonParser extends ShowdownPokemonParser {
    @Override
    protected void setPokemonLevel(Pokemon pokemon, int level) {
        pokemon.setLevel(RelativeLevelBattle.PIVOT + level);
    }
}
