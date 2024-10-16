package kiwiapollo.cobblemontrainerbattle.battlefactory;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Objects;

public class RandomPartyFactory {
    public static PlayerPartyStore create(ServerPlayerEntity player) {
        PlayerPartyStore party = new PlayerPartyStore(player.getUuid());

        party.add(PokemonSpecies.INSTANCE.random().create(BattleFactory.LEVEL));
        party.add(PokemonSpecies.INSTANCE.random().create(BattleFactory.LEVEL));
        party.add(PokemonSpecies.INSTANCE.random().create(BattleFactory.LEVEL));

        party.toGappyList().stream().filter(Objects::nonNull).forEach(Pokemon::heal);

        return party;
    }
}
