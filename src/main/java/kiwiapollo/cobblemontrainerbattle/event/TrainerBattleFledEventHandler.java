package kiwiapollo.cobblemontrainerbattle.event;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.CustomTrainerBattleActor;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.stream.StreamSupport;

/**
 * Fleeing from PokemonBattle doesn't count as defeat.
 * BATTLE_VICTORY event does not fire as well.
 * @see PokemonBattle#checkFlee()
 * @see PokemonBattle#tick()
 */
public class TrainerBattleFledEventHandler implements ServerTickEvents.EndWorldTick {
    @Override
    public void onEndTick(ServerWorld world) {
        for (ServerPlayerEntity player : world.getPlayers()) {
            try {
                PokemonBattle battle = getPokemonBattle(player);

                if (!isTrainerBattle(battle)) {
                    return;
                }

                CustomTrainerBattleActor trainer = getCustomTrainerBattleActor(battle);

                Vec3d playerPos = player.getPos();
                Vec3d trainerPos = trainer.getWorldAndPosition().getSecond();

                if (playerPos.distanceTo(trainerPos) < trainer.getFleeDistance()) {
                    return;
                }

                trainer.onPlayerDefeat();
                battle.writeShowdownAction(String.format(">forcelose %s", battle.getActor(player).showdownId));
                battle.end();

            } catch (ClassCastException | NullPointerException | IndexOutOfBoundsException ignored) {

            }
        }
    }

    private PokemonBattle getPokemonBattle(ServerPlayerEntity player) {
        return Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(player);
    }

    private CustomTrainerBattleActor getCustomTrainerBattleActor(PokemonBattle battle) {
        return StreamSupport.stream(battle.getActors().spliterator(), false)
                .filter(actor -> actor instanceof CustomTrainerBattleActor)
                .map(actor -> (CustomTrainerBattleActor) actor).toList()
                .get(0);
    }

    private boolean isTrainerBattle(PokemonBattle battle) {
        return StreamSupport.stream(battle.getActors().spliterator(), false)
                .anyMatch(actor -> actor instanceof CustomTrainerBattleActor);
    }
}
