package kiwiapollo.cobblemontrainerbattle.event;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.FleeableBattleActor;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContextStorage;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.*;
import java.util.stream.StreamSupport;

/**
 * Fleeing from Pokémon battle doesn't count as defeat.
 * BATTLE_VICTORY event does not fire as well.
 */
public class TrainerBattleFledEventHandler implements ServerTickEvents.EndWorldTick {
    @Override
    public void onEndTick(ServerWorld world) {
        List<ServerPlayerEntity> players = world.getPlayers().stream().filter(this::isFledFromTrainerBattle).toList();

        players.stream().map(this::getTrainerBattle).forEach(TrainerBattle::onPlayerDefeat);
        players.stream().map(this::getPokemonBattle).forEach(PokemonBattle::end);

        players.forEach(player -> CobblemonTrainerBattle.LOGGER.info("Battle was fled: {}", player.getGameProfile().getName()));
    }

    private boolean isFledFromTrainerBattle(ServerPlayerEntity player) {
        try {
            PokemonBattle battle = getPokemonBattle(player);
            FleeableBattleActor trainer = getFleeableBattleActors(battle).get(0);

            Vec3d playerPos = player.getPos();
            Vec3d trainerPos = trainer.getWorldAndPosition().getSecond();

            return playerPos.distanceTo(trainerPos) > trainer.getFleeDistance();

        } catch (NullPointerException | IndexOutOfBoundsException | NoSuchElementException e) {
            return false;
        }
    }

    private TrainerBattle getTrainerBattle(ServerPlayerEntity player) {
        return BattleContextStorage.getInstance().getOrCreate(player.getUuid()).getTrainerBattle();
    }

    private PokemonBattle getPokemonBattle(ServerPlayerEntity player) {
        UUID battleId = getTrainerBattle(player).getBattleId();
        return Cobblemon.INSTANCE.getBattleRegistry().getBattle(battleId);
    }

    private List<FleeableBattleActor> getFleeableBattleActors(PokemonBattle battle) {
        return StreamSupport.stream(battle.getActors().spliterator(), false)
                .filter(actor -> actor instanceof FleeableBattleActor)
                .map(actor -> (FleeableBattleActor) actor)
                .toList();
    }
}
