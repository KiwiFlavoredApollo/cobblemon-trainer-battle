package kiwiapollo.cobblemontrainerbattle.event;

import kiwiapollo.cobblemontrainerbattle.entity.CustomEntityType;
import kiwiapollo.cobblemontrainerbattle.entity.NeutralTrainerEntity;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntity;
import kiwiapollo.cobblemontrainerbattle.gamerule.CustomGameRule;
import kiwiapollo.cobblemontrainerbattle.item.vsseeker.VsSeeker;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.minecraft.SharedConstants.TICKS_PER_SECOND;

public class NeutralTrainerEntitySpawner implements ServerTickEvents.EndWorldTick {
    private static final int MAXIMUM_RADIUS = 30;
    private static final int MINIMUM_RADIUS = 5;

    @Override
    public void onEndTick(ServerWorld world) {
        if (!isEntitySpawnTick(world.getServer())) {
            return;
        }

        for (ServerPlayerEntity player : world.getPlayers()) {
            if (!isLessThanMaximumPerPlayerTrainerCount(world, player)) {
                return;
            }

            if (!hasVsSeeker(player)) {
                return;
            }

            trySpawnEntity(world, player);
        }
    }

    private void trySpawnEntity(ServerWorld world, ServerPlayerEntity player) {
        try {
            TrainerEntity entity = new NeutralTrainerEntity(CustomEntityType.NEUTRAL_TRAINER, world);
            Identifier trainer = selectRandomVsSeeker(player).create();
            entity.setTrainer(trainer);

            BlockPos pos = getRandomSpawnPosition(world, player);
            float yaw = Random.create().nextFloat() * 360f;
            float pitch = 0f;

            entity.refreshPositionAndAngles(pos, yaw, pitch);
            world.spawnEntity(entity);

        } catch (IndexOutOfBoundsException | IllegalStateException ignored) {

        }
    }

    private boolean isEntitySpawnTick(MinecraftServer server) {
        int interval = server.getGameRules().get(CustomGameRule.TRAINER_SPAWN_INTERVAL_IN_SECONDS).get() * TICKS_PER_SECOND;
        return server.getTicks() % interval == 0;
    }

    private boolean isLessThanMaximumPerPlayerTrainerCount(ServerWorld world, ServerPlayerEntity player) {
        int count = getEntityCount(world, player);
        int maximum = world.getServer().getGameRules().get(CustomGameRule.MAXIMUM_PER_PLAYER_TRAINER_COUNT).get();
        return count < maximum;
    }

    public int getEntityCount(ServerWorld world, ServerPlayerEntity player) {
        Box box = player.getBoundingBox().expand(MAXIMUM_RADIUS);
        return world.getEntitiesByType(CustomEntityType.NEUTRAL_TRAINER, box, entity -> true).size();
    }

    private boolean hasVsSeeker(ServerPlayerEntity player) {
        return !getVsSeekers(player.getInventory()).isEmpty();
    }

    private VsSeeker selectRandomVsSeeker(ServerPlayerEntity player) {
        List<VsSeeker> random = new ArrayList<>(getVsSeekers(player.getInventory()));
        Collections.shuffle(random);
        return random.get(0);
    }

    private List<VsSeeker> getVsSeekers(PlayerInventory inventory) {
        return inventory.combinedInventory.stream()
                .flatMap(DefaultedList::stream)
                .filter(stack -> !stack.isEmpty())
                .map(ItemStack::getItem)
                .filter(item -> item instanceof VsSeeker)
                .map(item -> (VsSeeker) item).toList();
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
