package kiwiapollo.cobblemontrainerbattle.parser.pokemon;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.preset.FlatLevelBattlePreset;

public class FlatLevelShowdownPokemonParser extends ShowdownPokemonParser {
    @Override
    protected void setPokemonLevel(Pokemon pokemon, int level) {
        pokemon.setLevel(FlatLevelBattlePreset.LEVEL);
    }
}
