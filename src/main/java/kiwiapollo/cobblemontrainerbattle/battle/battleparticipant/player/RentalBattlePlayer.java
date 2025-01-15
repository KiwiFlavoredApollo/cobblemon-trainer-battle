package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player;

import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.parser.player.BattleContextStorage;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Objects;

public class RentalBattlePlayer extends AbstractPlayerBattleParticipant {
    private static final int LEVEL = 50;

    public RentalBattlePlayer(ServerPlayerEntity player) {
        super(player, createFlatLevelClonedParty(player));
    }

    private static PartyStore createFlatLevelClonedParty(ServerPlayerEntity player) {
        PartyStore original = BattleContextStorage.getInstance().getOrCreate(player.getUuid()).getRentalPokemon();
        PartyStore clone = new PartyStore(player.getUuid());

        for (Pokemon pokemon : original.toGappyList().stream().filter(Objects::nonNull).toList()) {
            clone.add(pokemon.clone(true, true));
        }

        clone.toGappyList().stream().filter(Objects::nonNull).forEach(Pokemon::heal);
        clone.toGappyList().stream().filter(Objects::nonNull).forEach(pokemon -> pokemon.setLevel(LEVEL));

        return clone;
    }
}
