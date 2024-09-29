package kiwiapollo.cobblemontrainerbattle.battleactors.player;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battlefrontier.BattleFrontier;
import kiwiapollo.cobblemontrainerbattle.battlefrontier.BattleFrontierSession;
import kotlin.Unit;
import net.minecraft.server.network.ServerPlayerEntity;

public class BattleFrontierPlayerBattleActorFactory {
    public BattleActor create(ServerPlayerEntity player) {
        BattleFrontierSession session = BattleFrontier.SESSIONS.get(player.getUuid());
        session.partyPokemons.forEach(Pokemon::heal);
        session.partyPokemons.forEach(pokemon -> pokemon.setLevel(BattleFrontier.LEVEL));

        return new PlayerBattleActor(
                player.getUuid(),
                session.partyPokemons.stream()
                        .map(pokemon -> new BattlePokemon(
                                pokemon,
                                pokemon.clone(true, true),
                                pokemonEntity -> {
                                    pokemonEntity.discard();
                                    return Unit.INSTANCE;
                                }
                        ))
                        .toList()
        );
    }
}
