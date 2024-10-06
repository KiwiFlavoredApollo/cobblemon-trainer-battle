package kiwiapollo.cobblemontrainerbattle.events;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.entities.TrainerEntity;
import kiwiapollo.cobblemontrainerbattle.exceptions.TrainerSpawnException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;

public class TrainerSpawnEventHandler {
    private static final int SPAWN_INTERVAL = 100;
    private static final int RADIUS = 20;
    private static final int MAXIMUM_TRAINER_COUNT = 1;
    private static int tickCounter = 0;

    public static void onEndWorldTick(ServerWorld world) {
        tickCounter++;

        if (tickCounter >= SPAWN_INTERVAL) {
            for (PlayerEntity player : world.getPlayers()) {
                spawnTrainersAroundPlayer(world, player);
            }
            tickCounter = 0;
        }
    }

    private static void spawnTrainersAroundPlayer(ServerWorld world, PlayerEntity player) {
        try {
            assertBelowMaximumTrainerCount(world, player);

            BlockPos spawnPos = getSafeSpawnPosition(world, player);
            TrainerEntity trainerEntity = new TrainerEntity(CobblemonTrainerBattle.TRAINER_ENTITY_TYPE, world);
            trainerEntity.refreshPositionAndAngles(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(),
                    player.getYaw(), player.getPitch());
            world.spawnEntity(trainerEntity);

            CobblemonTrainerBattle.LOGGER.info("Spawned trainer");

        } catch (TrainerSpawnException ignored) {

        }
    }

    private static void assertBelowMaximumTrainerCount(ServerWorld world, PlayerEntity player) throws TrainerSpawnException {
        int trainerCount = world.getEntitiesByType(CobblemonTrainerBattle.TRAINER_ENTITY_TYPE,
                player.getBoundingBox().expand(RADIUS), entity -> true).size();
        if (trainerCount > MAXIMUM_TRAINER_COUNT) {
            throw new TrainerSpawnException();
        }
    }

    private static BlockPos getSafeSpawnPosition(ServerWorld world, PlayerEntity player) {
        BlockPos playerPos = player.getBlockPos();

        int xOffset = (int) (Math.random() * RADIUS * 2) - RADIUS;
        int zOffset = (int) (Math.random() * RADIUS * 2) - RADIUS;

        return world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, playerPos.add(xOffset, 0, zOffset));
    }
}
