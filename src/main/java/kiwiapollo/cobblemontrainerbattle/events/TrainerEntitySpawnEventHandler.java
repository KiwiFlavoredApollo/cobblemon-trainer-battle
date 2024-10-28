package kiwiapollo.cobblemontrainerbattle.events;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.entities.EntityTypes;
import kiwiapollo.cobblemontrainerbattle.entities.TrainerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class TrainerEntitySpawnEventHandler {
    private static final int SPAWN_INTERVAL = 1200;
    private static final int MAXIMUM_RADIUS = 30;
    private static final int MINIMUM_RADIUS = 5;
    private static final int MAXIMUM_TRAINER_COUNT = 1;

    public static void periodicallySpawnTrainerEntity(ServerWorld world) {
        if (!CobblemonTrainerBattle.config.enableTrainerSpawn) {
            return;
        }

        if (world.getServer().getTicks() % SPAWN_INTERVAL == 0) {
            for (PlayerEntity player : world.getPlayers()) {
                spawnTrainersAroundPlayer(world, player);
            }
        }
    }

    private static void spawnTrainersAroundPlayer(ServerWorld world, PlayerEntity player) {
        try {
            assertBelowMaximumTrainerCount(world, player);

            BlockPos spawnPos = getRandomSpawnPosition(world, player);
            TrainerEntity trainerEntity = new TrainerEntity(EntityTypes.TRAINER, world);
            trainerEntity.refreshPositionAndAngles(spawnPos, player.getYaw(), player.getPitch());
            world.spawnEntity(trainerEntity);

            CobblemonTrainerBattle.LOGGER.info("Spawned trainer on {} {}", world.getRegistryKey().getValue(), spawnPos.toString());

        } catch (AssertionError | IllegalStateException ignored) {

        }
    }

    private static void assertBelowMaximumTrainerCount(ServerWorld world, PlayerEntity player) throws AssertionError {
        int trainerCount = world.getEntitiesByType(EntityTypes.TRAINER,
                player.getBoundingBox().expand(MAXIMUM_RADIUS), entity -> true).size();
        if (trainerCount >= MAXIMUM_TRAINER_COUNT) {
            throw new AssertionError();
        }
    }

    private static BlockPos getRandomSpawnPosition(ServerWorld world, PlayerEntity player) throws IllegalStateException {
        BlockPos playerPos = player.getBlockPos();
        final int MAXIMUM_RETRIES = 50;

        for (int i = 0; i < MAXIMUM_RETRIES; i++) {
            int xOffset = Random.create().nextBetween(MINIMUM_RADIUS, MAXIMUM_RADIUS) * Random.create().nextBetween(-1, 1);
            int zOffset = Random.create().nextBetween(MINIMUM_RADIUS, MAXIMUM_RADIUS) * Random.create().nextBetween(-1, 1);
            int yOffset = Random.create().nextBetween(-1 * MAXIMUM_RADIUS, MAXIMUM_RADIUS);

            BlockPos spawnPos = playerPos.add(xOffset, yOffset, zOffset);
            boolean isSolidGround = world.getBlockState(spawnPos.down()).isSolidBlock(world, spawnPos.down());
            boolean isEmptySpace = world.getBlockState(spawnPos).isAir();

            if (isSolidGround && isEmptySpace) {
                return spawnPos;
            }
        }

        throw new IllegalStateException();
    }
}
