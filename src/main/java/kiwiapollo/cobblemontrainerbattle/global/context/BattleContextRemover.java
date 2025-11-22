package kiwiapollo.cobblemontrainerbattle.global.context;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.CustomTrainerBattleActor;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class BattleContextRemover implements ServerPlayConnectionEvents.Disconnect {
    @Override
    public void onPlayDisconnect(ServerPlayNetworkHandler handler, MinecraftServer server) {
        ServerPlayerEntity player = handler.getPlayer();

        forceTrainerBattlePlayerDefeat(player);

        BattleContextStorage.getInstance().remove(player.getUuid());
    }

    private void forceTrainerBattlePlayerDefeat(ServerPlayerEntity player) {
        try {
            PokemonBattle battle = Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(player);

            for (BattleActor actor : battle.getActors()) {
                if (actor instanceof CustomTrainerBattleActor) {
                    ((CustomTrainerBattleActor) actor).onPlayerDefeat();
                }
            }

        } catch (NullPointerException ignored) {

        }
    }
}
