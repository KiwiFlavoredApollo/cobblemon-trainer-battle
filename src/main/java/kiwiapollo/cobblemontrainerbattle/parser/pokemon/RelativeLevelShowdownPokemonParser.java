package kiwiapollo.cobblemontrainerbattle.parser.pokemon;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.preset.RelativeLevelBattlePreset;

public class RelativeLevelShowdownPokemonParser extends ShowdownPokemonParser {
    @Override
    protected void setPokemonLevel(Pokemon pokemon, int level) {
        pokemon.setLevel(RelativeLevelBattlePreset.PIVOT + level);
    }
}
