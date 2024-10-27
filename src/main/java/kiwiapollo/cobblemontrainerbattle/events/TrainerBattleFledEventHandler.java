package kiwiapollo.cobblemontrainerbattle.events;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.FleeableBattleActor;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleactor.EntityBackedTrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattleRegistry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.StreamSupport;

public class TrainerBattleFledEventHandler {
    public static void onEndWorldTick(ServerWorld world) {
        world.getPlayers().forEach(player -> {
            if (!TrainerBattleRegistry.containsKey(player.getUuid())) {
                return;
            }

            TrainerBattle trainerBattle = TrainerBattleRegistry.get(player.getUuid());
            PokemonBattle pokemonBattle = Cobblemon.INSTANCE.getBattleRegistry().getBattle(trainerBattle.getBattleId());
            if (!isFledBattle(pokemonBattle, player)) {
                return;
            }

            StreamSupport.stream(pokemonBattle.getActors().spliterator(), false)
                    .filter(battleActor -> battleActor instanceof EntityBackedTrainerBattleActor)
                    .map(battleActor -> ((EntityBackedTrainerBattleActor) battleActor))
                    .map(EntityBackedTrainerBattleActor::getEntity)
                    .filter(Objects::nonNull)
                    .forEach(trainerEntity -> trainerEntity.setAiDisabled(false));

            pokemonBattle.end();

            CobblemonTrainerBattle.LOGGER.info("Battle was fled: {}", player.getGameProfile().getName());
        });
    }

    private static boolean isFledBattle(PokemonBattle trainerBattle, ServerPlayerEntity player) {
        try {
            FleeableBattleActor trainerBattleActor =
                    StreamSupport.stream(trainerBattle.getActors().spliterator(), false)
                            .filter(battleActor -> battleActor instanceof FleeableBattleActor)
                            .map(battleActor -> (FleeableBattleActor) battleActor).findFirst().get();

            Vec3d playerPos = player.getPos();
            Vec3d trainerPos = trainerBattleActor.getWorldAndPosition().getSecond();

            return playerPos.distanceTo(trainerPos) > trainerBattleActor.getFleeDistance();

        } catch (NullPointerException | NoSuchElementException | ClassCastException e) {
            return false;
        }
    }
}
