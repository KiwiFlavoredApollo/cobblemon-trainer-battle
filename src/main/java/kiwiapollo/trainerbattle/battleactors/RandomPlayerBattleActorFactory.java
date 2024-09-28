package kiwiapollo.trainerbattle.battleactors;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kotlin.Unit;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class RandomPlayerBattleActorFactory {
    public BattleActor create(ServerPlayerEntity player) {
        List<Pokemon> playerParty = List.of(
            PokemonSpecies.INSTANCE.random().create(20),
            PokemonSpecies.INSTANCE.random().create(20),
            PokemonSpecies.INSTANCE.random().create(20)
        );

        return new PlayerBattleActor(
                player.getUuid(),
                playerParty.stream()
                        .map(pokemon -> new BattlePokemon(
                                pokemon,
                                pokemon,
                                pokemonEntity -> {
                                    pokemonEntity.discard();
                                    return Unit.INSTANCE;
                                }
                        ))
                        .toList()
        );
    }
}
