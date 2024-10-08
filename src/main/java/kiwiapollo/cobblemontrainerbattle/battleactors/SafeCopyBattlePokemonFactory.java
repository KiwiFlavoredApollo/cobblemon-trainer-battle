package kiwiapollo.cobblemontrainerbattle.battleactors;

import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kotlin.Unit;

public class SafeCopyBattlePokemonFactory {
    public static BattlePokemon create(Pokemon pokemon) {
        return new BattlePokemon(
                pokemon,
                pokemon.clone(true, true),
                pokemonEntity -> {
                    pokemonEntity.discard();
                    return Unit.INSTANCE;
                }
        );
    }
}
