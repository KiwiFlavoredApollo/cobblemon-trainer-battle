package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.global.context.RentalPokemon;
import kiwiapollo.cobblemontrainerbattle.global.context.RentalPokemonStorage;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class RentalBattlePlayer extends AbstractPlayerBattleParticipant {
    public RentalBattlePlayer(ServerPlayerEntity player) {
        super(player, getRentalPokemon(player));
    }

    private static PartyStore getRentalPokemon(ServerPlayerEntity player) {
        RentalPokemon rental = RentalPokemonStorage.getInstance().get(player);
        PartyStore party = new PartyStore(player.getUuid());
        rental.stream().forEach(party::add);

        return party;
    }

    @Override
    public BattleActor createBattleActor() {
        return new PlayerBattleActor(
                getUuid(),
                getBattleTeam()
        );
    }

    private List<BattlePokemon> getBattleTeam() {
        return getParty().toBattleTeam(true, false, null);
    }
}
