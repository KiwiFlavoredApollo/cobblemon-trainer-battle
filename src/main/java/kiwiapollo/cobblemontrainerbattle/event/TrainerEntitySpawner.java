package kiwiapollo.cobblemontrainerbattle.event;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.config.ConfigStorage;
import kiwiapollo.cobblemontrainerbattle.entity.RandomSpawnableTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.entity.EntityTypes;
import kiwiapollo.cobblemontrainerbattle.entity.RandomTrainerEntityFactory;
import kiwiapollo.cobblemontrainerbattle.entity.RandomTrainerTextureFactory;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntity;
import kiwiapollo.cobblemontrainerbattle.item.MiscItems;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import java.util.Set;

public class TrainerEntitySpawner implements ServerTickEvents.EndWorldTick {
    private static final int SPAWN_INTERVAL = 60;
    private static final int MAXIMUM_RADIUS = 30;
    private static final int MINIMUM_RADIUS = 5;

    @Override
    public void onEndTick(ServerWorld world) {
        if (world.getServer().getTicks() % SPAWN_INTERVAL == 0) {
            for (PlayerEntity player : world.getPlayers()) {
                spawnTrainersAroundPlayer(world, player);
            }
        }
    }

    private void spawnTrainersAroundPlayer(ServerWorld world, PlayerEntity player) {
        try {
            if (!containsVsSeeker(player.getInventory())) {
                return;
            }

            if (!isBelowMaximumTrainerCount(world, player)) {
                return;
            }

            RandomTrainerEntityFactory factory = createRandomTrainerEntityFactory(player.getInventory());
            TrainerEntity entity = factory.create(EntityTypes.TRAINER, world);

            BlockPos spawnPos = getRandomSpawnPosition(world, player);
            entity.refreshPositionAndAngles(spawnPos, player.getYaw(), player.getPitch());
            world.spawnEntity(entity);

            CobblemonTrainerBattle.LOGGER.info("Spawned trainer on {} {}", world.getRegistryKey().getValue(), spawnPos);

        } catch (IllegalStateException ignored) {
            
        }
    }

    private RandomTrainerEntityFactory createRandomTrainerEntityFactory(Inventory inventory) {
        RandomSpawnableTrainerFactory trainer = createSpawnableRandomTrainerFactory(inventory);
        RandomTrainerTextureFactory texture = new RandomTrainerTextureFactory();

        return new RandomTrainerEntityFactory(trainer, texture);
    }

    private RandomSpawnableTrainerFactory createSpawnableRandomTrainerFactory(Inventory inventory) {
        RandomSpawnableTrainerFactory.Builder builder = new RandomSpawnableTrainerFactory.Builder();
        if (inventory.containsAny(Set.of(MiscItems.BLUE_VS_SEEKER))) {
            builder = builder.addAll();
        }

        if (inventory.containsAny(Set.of(MiscItems.RED_VS_SEEKER))) {
            builder = builder.addRadicalRed();
        }

        if (inventory.containsAny(Set.of(MiscItems.GREEN_VS_SEEKER))) {
            builder = builder.addInclementEmerald();
        }

        if (inventory.containsAny(Set.of(MiscItems.PURPLE_VS_SEEKER))) {
            builder = builder.addSmogon();
        }

        return builder.build();
    }

    private boolean containsVsSeeker(Inventory inventory) {
        return inventory.containsAny(Set.of(
                MiscItems.BLUE_VS_SEEKER,
                MiscItems.RED_VS_SEEKER,
                MiscItems.GREEN_VS_SEEKER,
                MiscItems.PURPLE_VS_SEEKER
        ));
    }

    private boolean isBelowMaximumTrainerCount(ServerWorld world, PlayerEntity player) {
        int count = world.getEntitiesByType(EntityTypes.TRAINER, player.getBoundingBox().expand(MAXIMUM_RADIUS), entity -> true).size();
        return count >= ConfigStorage.getInstance().getMaximumTrainerSpawnCount();
    }

    private BlockPos getRandomSpawnPosition(ServerWorld world, PlayerEntity player) throws IllegalStateException {
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
