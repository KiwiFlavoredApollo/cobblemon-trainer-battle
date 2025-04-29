package kiwiapollo.cobblemontrainerbattle.event;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.entity.NormalTrainerEntity;
import kiwiapollo.cobblemontrainerbattle.global.config.ConfigStorage;
import kiwiapollo.cobblemontrainerbattle.entity.RandomSpawnableTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.entity.EntityType;
import kiwiapollo.cobblemontrainerbattle.entity.RandomNormalTrainerEntityFactory;
import kiwiapollo.cobblemontrainerbattle.item.VsSeeker;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static net.minecraft.SharedConstants.TICKS_PER_SECOND;

public class TrainerEntitySpawner implements ServerTickEvents.EndWorldTick {
    private static final int MAXIMUM_RADIUS = 30;
    private static final int MINIMUM_RADIUS = 5;

    @Override
    public void onEndTick(ServerWorld world) {
        if (isTrainerSpawnTick(world.getServer().getTicks())) {
            for (ServerPlayerEntity player : world.getPlayers()) {
                spawnTrainersAroundPlayer(world, player);
            }
        }
    }

    private boolean isTrainerSpawnTick(int ticks) {
        int interval = ConfigStorage.getInstance().getTrainerSpawnIntervalInSeconds() * TICKS_PER_SECOND;
        return ticks % interval == 0;
    }

    private void spawnTrainersAroundPlayer(ServerWorld world, ServerPlayerEntity player) {
        try {
            if (getVsSeekers(player.getInventory()).isEmpty()) {
                return;
            }

            if (!isBelowMaximumTrainerSpawnCount(world, player)) {
                return;
            }

            Predicate<String> predicate = toPredicate(player.getInventory());
            SimpleFactory<String> trainerFactory = new RandomSpawnableTrainerFactory(predicate);
            RandomNormalTrainerEntityFactory entityFactory = new RandomNormalTrainerEntityFactory(trainerFactory);
            NormalTrainerEntity entity = entityFactory.create(EntityType.NORMAL_TRAINER, world);

            BlockPos spawnPos = getRandomSpawnPosition(world, player);
            entity.refreshPositionAndAngles(spawnPos, player.getYaw(), player.getPitch());
            world.spawnEntity(entity);

            CobblemonTrainerBattle.LOGGER.info("Spawned trainer on {} {}", world.getRegistryKey().getValue(), spawnPos);

        } catch (IllegalStateException ignored) {
            
        }
    }

    private Predicate<String> toPredicate(PlayerInventory inventory) {
        List<Predicate<String>> predicates = new ArrayList<>();

        predicates.add(trainer -> false);
        predicates.addAll(getVsSeekers(inventory));

        return predicates.stream().reduce(Predicate::or).orElse(t -> true);
    }

    private List<VsSeeker> getVsSeekers(PlayerInventory inventory) {
        return inventory.combinedInventory.stream()
                .flatMap(DefaultedList::stream)
                .filter(stack -> !stack.isEmpty())
                .map(ItemStack::getItem)
                .filter(item -> item instanceof VsSeeker)
                .map(item -> (VsSeeker) item).toList();
    }

    private boolean isBelowMaximumTrainerSpawnCount(ServerWorld world, PlayerEntity player) {
        int count = world.getEntitiesByType(EntityType.NORMAL_TRAINER, player.getBoundingBox().expand(MAXIMUM_RADIUS), entity -> true).size();
        return count < ConfigStorage.getInstance().getMaximumTrainerSpawnCount();
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
