package kiwiapollo.cobblemontrainerbattle.battleactors.player;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battlefactory.BattleFactory;
import kiwiapollo.cobblemontrainerbattle.battlefactory.BattleFactorySession;
import kiwiapollo.cobblemontrainerbattle.common.SafeCopyBattlePokemonFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public class BattleFactoryPlayerBattleActorFactory {
    public BattleActor create(ServerPlayerEntity player) {
        BattleFactorySession session = BattleFactory.sessions.get(player.getUuid());
        session.partyPokemons.forEach(Pokemon::heal);
        session.partyPokemons.forEach(pokemon -> pokemon.setLevel(BattleFactory.LEVEL));

        return new PlayerBattleActor(
                player.getUuid(),
                session.partyPokemons.stream().map(SafeCopyBattlePokemonFactory::create).toList()
        );
    }
}
