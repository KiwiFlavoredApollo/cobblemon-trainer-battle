package kiwiapollo.cobblemontrainerbattle.events;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.FleeableBattleActor;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battlefactory.BattleFactory;
import kiwiapollo.cobblemontrainerbattle.entities.EntityBackedTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.groupbattle.GroupBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.NoSuchElementException;
import java.util.stream.StreamSupport;

public class TrainerBattleFledEventHandler {
    public static void onEndWorldTick(ServerWorld world) {
        world.getPlayers().forEach(player -> {
            if (GroupBattle.trainerBattles.containsKey(player.getUuid())
                    && isFledBattle(GroupBattle.trainerBattles.get(player.getUuid()), player)) {
                GroupBattle.trainerBattles.get(player.getUuid()).end();
                GroupBattle.trainerBattles.remove(player.getUuid());
                GroupBattle.sessions.get(player.getUuid()).isDefeated = true;

                CobblemonTrainerBattle.LOGGER.info(String.format("Battle was fled: %s", player.getGameProfile().getName()));
            }

            if (BattleFactory.trainerBattles.containsKey(player.getUuid())
                    && isFledBattle(BattleFactory.trainerBattles.get(player.getUuid()), player)) {
                BattleFactory.trainerBattles.get(player.getUuid()).end();
                BattleFactory.trainerBattles.remove(player.getUuid());
                BattleFactory.sessions.get(player.getUuid()).isDefeated = true;

                CobblemonTrainerBattle.LOGGER.info(String.format("Battle was fled: %s", player.getGameProfile().getName()));
            }

            if (EntityBackedTrainerBattle.trainerBattles.containsKey(player.getUuid())
                    && isFledBattle(EntityBackedTrainerBattle.trainerBattles.get(player.getUuid()), player)) {
                EntityBackedTrainerBattle.trainerBattles.get(player.getUuid()).end();
                EntityBackedTrainerBattle.trainerBattles.remove(player.getUuid());

                CobblemonTrainerBattle.LOGGER.info(String.format("Battle was fled: %s", player.getGameProfile().getName()));
            }

            if (TrainerBattle.trainerBattles.containsKey(player.getUuid())
                    && isFledBattle(TrainerBattle.trainerBattles.get(player.getUuid()), player)) {
                TrainerBattle.trainerBattles.get(player.getUuid()).end();
                TrainerBattle.trainerBattles.remove(player.getUuid());

                CobblemonTrainerBattle.LOGGER.info(String.format("Battle was fled: %s", player.getGameProfile().getName()));
            }
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
