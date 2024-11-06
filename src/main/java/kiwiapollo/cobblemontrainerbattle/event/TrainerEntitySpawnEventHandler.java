package kiwiapollo.cobblemontrainerbattle.event;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.entity.EntityTypes;
import kiwiapollo.cobblemontrainerbattle.entity.RandomTrainerEntityFactory;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntity;
import kiwiapollo.cobblemontrainerbattle.item.ItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import java.util.Set;

public class TrainerEntitySpawnEventHandler {
    private static final int SPAWN_INTERVAL = 1200;
    private static final int MAXIMUM_RADIUS = 30;
    private static final int MINIMUM_RADIUS = 5;

    public static void periodicallySpawnTrainerEntity(ServerWorld world) {
        if (world.getServer().getTicks() % SPAWN_INTERVAL == 0) {
            for (PlayerEntity player : world.getPlayers()) {
                spawnTrainersAroundPlayer(world, player);
            }
        }
    }

    private static void spawnTrainersAroundPlayer(ServerWorld world, PlayerEntity player) {
        try {
            assertPlayerHasVsSeeker(player);
            assertBelowMaximumTrainerCount(world, player);

            BlockPos spawnPos = getRandomSpawnPosition(world, player);

            EntityType.EntityFactory<TrainerEntity> factory = createTrainerEntityFactory(player.getInventory());
            TrainerEntity entity = factory.create(EntityTypes.TRAINER, world);

            entity.refreshPositionAndAngles(spawnPos, player.getYaw(), player.getPitch());
            world.spawnEntity(entity);

            CobblemonTrainerBattle.LOGGER.info("Spawned trainer on {} {}", world.getRegistryKey().getValue(), spawnPos);

        } catch (IllegalStateException ignored) {

        }
    }

    private static void assertPlayerHasVsSeeker(PlayerEntity player) {
        boolean hasVsSeeker = player.getInventory().containsAny(Set.of(
                ItemRegistry.BLUE_VS_SEEKER,
                ItemRegistry.RED_VS_SEEKER,
                ItemRegistry.GREEN_VS_SEEKER,
                ItemRegistry.PURPLE_VS_SEEKER
        ));

        if (!hasVsSeeker) {
            throw new IllegalStateException();
        }
    }

    private static EntityType.EntityFactory<TrainerEntity> createTrainerEntityFactory(Inventory inventory) {
        RandomTrainerEntityFactory.Builder builder = new RandomTrainerEntityFactory.Builder();

        boolean hasBlueVsSeeker = inventory.containsAny(Set.of(ItemRegistry.BLUE_VS_SEEKER));
        if (hasBlueVsSeeker) {
            builder = builder.addWildcard();
        }

        boolean hasRedVsSeeker = inventory.containsAny(Set.of(ItemRegistry.RED_VS_SEEKER));
        if (hasRedVsSeeker) {
            builder = builder.addRadicalRed();
        }

        boolean hasGreenVsSeeker = inventory.containsAny(Set.of(ItemRegistry.GREEN_VS_SEEKER));
        if (hasGreenVsSeeker) {
            builder = builder.addInclementEmerald();
        }

        boolean hasPurpleVsSeeker = inventory.containsAny(Set.of(ItemRegistry.PURPLE_VS_SEEKER));
        if (hasPurpleVsSeeker) {
            builder = builder.addSmogon();
        }

        return builder.build();
    }

    private static void assertBelowMaximumTrainerCount(ServerWorld world, PlayerEntity player) {
        int trainerCount = world.getEntitiesByType(EntityTypes.TRAINER,
                player.getBoundingBox().expand(MAXIMUM_RADIUS), entity -> true).size();
        if (trainerCount >= CobblemonTrainerBattle.config.maximumTrainerSpawnCount) {
            throw new IllegalStateException();
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
