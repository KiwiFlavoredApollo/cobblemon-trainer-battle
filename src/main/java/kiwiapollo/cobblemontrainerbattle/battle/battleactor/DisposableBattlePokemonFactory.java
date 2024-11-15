package kiwiapollo.cobblemontrainerbattle.battle.battleactor;

import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kotlin.Unit;

public class DisposableBattlePokemonFactory {
    public static BattlePokemon create(Pokemon pokemon) {
        return new BattlePokemon(
                pokemon,
                pokemon,
                pokemonEntity -> {
                    // Calling discard() does not prevent LOOT_DROPPED event
                    pokemonEntity.discard();
                    return Unit.INSTANCE;
                }
        );
    }
}
