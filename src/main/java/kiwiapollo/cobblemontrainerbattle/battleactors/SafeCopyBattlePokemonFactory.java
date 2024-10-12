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
                    // Discards the entity. This is also referred to as "despawning".
                    // This does not cause the entity to drop loot. - JavaDoc
                    //
                    // This does not apply to Pokemon drops defined in `resources/data/cobblemon/species/`
                    // LOOT_DROPPED event fires regardless of discarding PokemonEntity
                    pokemonEntity.discard();
                    return Unit.INSTANCE;
                }
        );
    }
}
