package kiwiapollo.cobblemontrainerbattle.event;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.entity.*;
import kiwiapollo.cobblemontrainerbattle.item.vsseeker.VsSeeker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public abstract class TrainerEntitySpawner implements WeightedEntitySpawner, EntityCounter {
    protected static final int MAXIMUM_RADIUS = 30;
    protected static final int MINIMUM_RADIUS = 5;

    @Override
    public void spawnEntity(ServerWorld world, ServerPlayerEntity player) {
        try {
            TrainerEntity entity = createTrainerEntity(world, player);
            BlockPos spawnPos = getRandomSpawnPosition(world, player);
            entity.refreshPositionAndAngles(spawnPos, player.getYaw(), player.getPitch());
            world.spawnEntity(entity);

             CobblemonTrainerBattle.LOGGER.info("Spawned {} at {} {}", entity.getDisplayName().getString(), world.getRegistryKey().getValue(), spawnPos);

        } catch (ClassCastException | IllegalStateException ignored) {

        }
    }

    protected abstract TrainerEntity createTrainerEntity(ServerWorld world, ServerPlayerEntity player);

    protected Predicate<Identifier> toPredicate(PlayerInventory inventory) {
        List<Predicate<Identifier>> predicates = new ArrayList<>();

        predicates.add(trainer -> false);
        predicates.addAll(VsSeeker.getVsSeekers(inventory));

        return predicates.stream().reduce(Predicate::or).orElse(t -> true);
    }

    private BlockPos getRandomSpawnPosition(ServerWorld world, PlayerEntity player) throws IllegalStateException {
        BlockPos playerPos = player.getBlockPos();
        final int MAXIMUM_RETRIES = 50;

        for (int i = 0; i < MAXIMUM_RETRIES; i++) {
            int xOffset = Random.create().nextBetween(MINIMUM_RADIUS, MAXIMUM_RADIUS) * getRandomSign();
            int zOffset = Random.create().nextBetween(MINIMUM_RADIUS, MAXIMUM_RADIUS) * getRandomSign();
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

    private int getRandomSign() {
        List<Integer> random = new ArrayList<>(List.of(-1, 1));
        Collections.shuffle(random);
        return random.get(0);
    }
}
