package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class NormalLevelPlayer extends AbstractPlayerBattleParticipant {
    public NormalLevelPlayer(ServerPlayerEntity player) {
        super(player, Cobblemon.INSTANCE.getStorage().getParty(player));
    }
}
