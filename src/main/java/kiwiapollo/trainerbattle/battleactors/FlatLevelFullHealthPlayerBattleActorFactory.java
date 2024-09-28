package kiwiapollo.trainerbattle.battleactors;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Objects;
import java.util.UUID;

public class FlatLevelFullHealthPlayerBattleActorFactory {
    public BattleActor create(ServerPlayerEntity player) {
        PartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        UUID leadingPokemon = playerPartyStore.toGappyList().stream()
                .filter(Objects::nonNull)
                .findFirst().get().getUuid();

        return new PlayerBattleActor(
                player.getUuid(),
                playerPartyStore.toBattleTeam(
                        true,
                        true,
                        leadingPokemon
                )
        );
    }
}
