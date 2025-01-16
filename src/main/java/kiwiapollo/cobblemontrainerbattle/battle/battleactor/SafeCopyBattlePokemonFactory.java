package kiwiapollo.cobblemontrainerbattle.battle.battleactor;

import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kotlin.Unit;

import java.util.function.Function;

public class SafeCopyBattlePokemonFactory implements Function<Pokemon, BattlePokemon> {
    @Override
    public BattlePokemon apply(Pokemon pokemon) {
        return new BattlePokemon(
                pokemon,
                pokemon.clone(true, true),
                pokemonEntity -> {
                    // Calling discard() does not prevent LOOT_DROPPED event
                    pokemonEntity.discard();
                    return Unit.INSTANCE;
                }
        );
    }
}
