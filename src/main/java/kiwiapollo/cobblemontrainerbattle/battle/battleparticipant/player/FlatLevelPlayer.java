package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import kiwiapollo.cobblemontrainerbattle.battle.preset.FlatLevelBattlePreset;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class FlatLevelPlayer extends AbstractPlayerBattleParticipant {
    public FlatLevelPlayer(ServerPlayerEntity player) {
        super(player, Cobblemon.INSTANCE.getStorage().getParty(player));
    }

    @Override
    public BattleActor createBattleActor() {
        return new PlayerBattleActor(
                getUuid(),
                getBattleTeam()
        );
    }

    private List<BattlePokemon> getBattleTeam() {
        List<BattlePokemon> team = getParty().toBattleTeam(true, false, null);
        team.forEach(pokemon -> pokemon.getEffectedPokemon().setLevel(FlatLevelBattlePreset.LEVEL));
        return team;
    }
}
