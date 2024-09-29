package kiwiapollo.cobblemontrainerbattle.battleactors.player;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Objects;
import java.util.UUID;

public class StatusQuoPlayerBattleActorFactory {
    public BattleActor create(ServerPlayerEntity player) {
        PartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        UUID leadingPokemon = playerPartyStore.toGappyList().stream()
                .filter(Objects::nonNull)
                .filter(pokemon -> !pokemon.isFainted())
                .findFirst().get().getUuid();

        return new PlayerBattleActor(
                player.getUuid(),
                playerPartyStore.toBattleTeam(
                        false,
                        false,
                        leadingPokemon
                )
        );
    }
}
