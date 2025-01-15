package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.DisposableBattlePokemonFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FlatLevelPlayer extends AbstractPlayerBattleParticipant {
    private static final int LEVEL = 50;

    public FlatLevelPlayer(ServerPlayerEntity player) {
        super(player, createFlatLevelClonedParty(player));
    }

    private static PartyStore createFlatLevelClonedParty(ServerPlayerEntity player) {
        PartyStore original = Cobblemon.INSTANCE.getStorage().getParty(player);
        PartyStore clone = new PartyStore(player.getUuid());

        for (Pokemon pokemon : original.toGappyList().stream().filter(Objects::nonNull).toList()) {
            clone.add(pokemon.clone(true, true));
        }

        clone.toGappyList().stream().filter(Objects::nonNull).forEach(Pokemon::heal);
        clone.toGappyList().stream().filter(Objects::nonNull).forEach(pokemon -> pokemon.setLevel(LEVEL));

        return clone;
    }
}
