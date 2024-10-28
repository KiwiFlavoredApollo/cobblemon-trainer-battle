package kiwiapollo.cobblemontrainerbattle.events;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.entities.EntityTypes;
import kiwiapollo.cobblemontrainerbattle.entities.RegexTrainerEntityFactory;
import kiwiapollo.cobblemontrainerbattle.entities.TrainerEntity;
import kiwiapollo.cobblemontrainerbattle.item.ItemRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TrainerEntitySpawnEventHandler {
    private static final int SPAWN_INTERVAL = 1200;
    private static final int MAXIMUM_RADIUS = 30;
    private static final int MINIMUM_RADIUS = 5;
    private static final int MAXIMUM_TRAINER_COUNT = 1;

    public static void periodicallySpawnTrainerEntity(ServerWorld world) {
        if (world.getServer().getTicks() % SPAWN_INTERVAL == 0) {
            for (PlayerEntity player : world.getPlayers()) {
                spawnTrainersAroundPlayer(world, player);
            }
        }
    }

    private static void spawnTrainersAroundPlayer(ServerWorld world, PlayerEntity player) {
        try {
            assertTrainerSpawnEnabled();
            assertBelowMaximumTrainerCount(world, player);
            assertPlayerHasVsSeeker(player);

            BlockPos spawnPos = getRandomSpawnPosition(world, player);
            String regex = toRegex(player.getInventory());
            TrainerEntity trainerEntity = new RegexTrainerEntityFactory(regex).create(EntityTypes.TRAINER, world);
            trainerEntity.refreshPositionAndAngles(spawnPos, player.getYaw(), player.getPitch());
            world.spawnEntity(trainerEntity);

            CobblemonTrainerBattle.LOGGER.info("Spawned trainer on {} {}", world.getRegistryKey().getValue(), spawnPos);

        } catch (IllegalStateException ignored) {

        }
    }

    private static void assertTrainerSpawnEnabled() {
        if (!CobblemonTrainerBattle.config.enableTrainerSpawn) {
            throw new IllegalStateException();
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

    private static String toRegex(Inventory inventory) {
        final String WILDCARD = ".*";

        boolean hasBlueVsSeeker = inventory.containsAny(Set.of(ItemRegistry.BLUE_VS_SEEKER));
        if (hasBlueVsSeeker) {
            return WILDCARD;
        }

        List<String> patterns = new ArrayList<>();

        boolean hasRedVsSeeker = inventory.containsAny(Set.of(ItemRegistry.RED_VS_SEEKER));
        if (hasRedVsSeeker) {
            patterns.add("radicalred");
        }

        boolean hasGreenVsSeeker = inventory.containsAny(Set.of(ItemRegistry.GREEN_VS_SEEKER));
        if (hasGreenVsSeeker) {
            patterns.add("inclementemerald");
        }

        boolean hasPurpleVsSeeker = inventory.containsAny(Set.of(ItemRegistry.PURPLE_VS_SEEKER));
        if (hasPurpleVsSeeker) {
            patterns.add("smogon");
        }

        if (patterns.isEmpty()) {
            throw new IllegalStateException();
        }

        String group = "(" + String.join("|", patterns) + ")";

        return String.format("^%s/.+", group);
    }

    private static void assertBelowMaximumTrainerCount(ServerWorld world, PlayerEntity player) {
        int trainerCount = world.getEntitiesByType(EntityTypes.TRAINER,
                player.getBoundingBox().expand(MAXIMUM_RADIUS), entity -> true).size();
        if (trainerCount >= MAXIMUM_TRAINER_COUNT) {
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
