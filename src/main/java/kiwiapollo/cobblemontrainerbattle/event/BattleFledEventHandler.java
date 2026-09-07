package kiwiapollo.cobblemontrainerbattle.event;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import kiwiapollo.cobblemontrainerbattle.battle.TrainerBattleActor;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Fleeing from PokemonBattle doesn't count as defeat.
 * BATTLE_VICTORY event does not fire as well.
 * @see PokemonBattle#checkFlee()
 * @see PokemonBattle#tick()
 * @see PokemonBattle#checkForfeit()
 */
public class BattleFledEventHandler implements ServerTickEvents.EndWorldTick {
    public static void initialize() {
        ServerTickEvents.END_WORLD_TICK.register(new BattleFledEventHandler());
    }

    @Override
    public void onEndTick(ServerWorld world) {
        for (ServerPlayerEntity player : getPlayers(world)) {
            if (!isPlayerFledFromTrainerBattle(player)) {
                continue;
            }

            PokemonBattle battle = getPokemonBattle(player);
            TrainerBattleActor trainer = getTrainerBattleActor(battle);

            trainer.onPlayerDefeat();
            battle.writeShowdownAction(String.format(">forcelose %s", battle.getActor(player).showdownId));
            battle.end();
        }
    }

    private boolean isPlayerFledFromTrainerBattle(ServerPlayerEntity player) {
        try {
            PokemonBattle battle = getPokemonBattle(player);

            if (!isTrainerBattle(battle)) {
                return false;
            }

            TrainerBattleActor trainer = getTrainerBattleActor(battle);

            if (!isPlayerFled(player, trainer)) {
                return false;
            }

            return true;

        } catch (ClassCastException | NullPointerException | IndexOutOfBoundsException ignored) {
            return false;
        }
    }

    private List<ServerPlayerEntity> getPlayers(ServerWorld world) {
        return List.copyOf(world.getPlayers());
    }

    private boolean isPlayerFled(ServerPlayerEntity player, TrainerBattleActor trainer) {
        return !isSameWorld(player, trainer) || isFarAway(player, trainer);
    }

    private boolean isSameWorld(ServerPlayerEntity player, TrainerBattleActor trainer) {
        ServerWorld playerWorld = player.getServerWorld();
        ServerWorld trainerWorld = trainer.getWorldAndPosition().getFirst();

        return playerWorld == trainerWorld;
    }

    private boolean isFarAway(ServerPlayerEntity player, TrainerBattleActor trainer) {
        Vec3d playerPos = player.getPos();
        Vec3d trainerPos = trainer.getWorldAndPosition().getSecond();

        return playerPos.distanceTo(trainerPos) > trainer.getFleeDistance();
    }

    private PokemonBattle getPokemonBattle(ServerPlayerEntity player) {
        return Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(player);
    }

    private TrainerBattleActor getTrainerBattleActor(PokemonBattle battle) {
        return StreamSupport.stream(battle.getActors().spliterator(), false)
                .filter(actor -> actor instanceof TrainerBattleActor)
                .map(actor -> (TrainerBattleActor) actor).toList()
                .get(0);
    }

    private boolean isTrainerBattle(PokemonBattle battle) {
        return StreamSupport.stream(battle.getActors().spliterator(), false)
                .anyMatch(actor -> actor instanceof TrainerBattleActor);
    }
}
