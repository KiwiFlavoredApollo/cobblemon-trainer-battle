package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import kiwiapollo.cobblemontrainerbattle.parser.player.BattleContextStorage;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class RentalBattlePlayer extends AbstractPlayerBattleParticipant {
    private static final int LEVEL = 50;

    public RentalBattlePlayer(ServerPlayerEntity player) {
        super(player, BattleContextStorage.getInstance().getOrCreate(player.getUuid()).getRentalPokemon());
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
