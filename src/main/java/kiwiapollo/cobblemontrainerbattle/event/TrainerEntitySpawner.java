package kiwiapollo.cobblemontrainerbattle.event;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.entity.*;
import kiwiapollo.cobblemontrainerbattle.item.VsSeeker;
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

public abstract class TrainerEntitySpawner implements WeightedEntitySpawner {
    protected static final int MAXIMUM_RADIUS = 30;
    protected static final int MINIMUM_RADIUS = 5;

    @Override
    public void spawnEntity(ServerWorld world, ServerPlayerEntity player) {
        try {
            TrainerEntity entity = createTrainerEntity(world, player);
            BlockPos spawnPos = getRandomSpawnPosition(world, player);
            entity.refreshPositionAndAngles(spawnPos, player.getYaw(), player.getPitch());
            world.spawnEntity(entity);

            CobblemonTrainerBattle.LOGGER.info("Spawned {} on {} {}", entity.getName(), world.getRegistryKey().getValue(), spawnPos);

        } catch (ClassCastException | IllegalStateException ignored) {

        }
    }

    private List<VsSeeker> getVsSeekers(PlayerInventory inventory) {
        return inventory.combinedInventory.stream()
                .flatMap(DefaultedList::stream)
                .filter(stack -> !stack.isEmpty())
                .map(ItemStack::getItem)
                .filter(item -> item instanceof VsSeeker)
                .map(item -> (VsSeeker) item).toList();
    }

    protected abstract TrainerEntity createTrainerEntity(ServerWorld world, ServerPlayerEntity player);

    protected Predicate<String> toPredicate(PlayerInventory inventory) {
        List<Predicate<String>> predicates = new ArrayList<>();

        predicates.add(trainer -> false);
        predicates.addAll(getVsSeekers(inventory));

        return predicates.stream().reduce(Predicate::or).orElse(t -> true);
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
