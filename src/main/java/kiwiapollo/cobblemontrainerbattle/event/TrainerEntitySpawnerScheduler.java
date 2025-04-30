package kiwiapollo.cobblemontrainerbattle.event;

import kiwiapollo.cobblemontrainerbattle.global.config.ConfigStorage;
import kiwiapollo.cobblemontrainerbattle.item.VsSeeker;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.SharedConstants.TICKS_PER_SECOND;

public class TrainerEntitySpawnerScheduler implements ServerTickEvents.EndWorldTick {
    private static final List<TrainerEntitySpawner> SPAWNERS = List.of(
            new NeutralTrainerEntitySpawner(90),
            new HostileTrainerEntitySpawner(10)
    );

    @Override
    public void onEndTick(ServerWorld world) {
        if (!isTrainerSpawnTick(world.getServer().getTicks())) {
            return;
        }

        for (ServerPlayerEntity player : world.getPlayers()) {
            if (!hasVsSeeker(player)) {
                continue;
            }

            if (!isBelowMaximumTrainerSpawnCount(world, player)) {
                continue;
            }

            getRandomSpawner(getSpawners()).spawnEntity(world, player);
        }
    }

    private boolean isTrainerSpawnTick(int ticks) {
        int interval = ConfigStorage.getInstance().getTrainerSpawnIntervalInSeconds() * TICKS_PER_SECOND;
        return ticks % interval == 0;
    }

    private boolean hasVsSeeker(ServerPlayerEntity player) {
        return !VsSeeker.getVsSeekers(player.getInventory()).isEmpty();
    }

    private boolean isBelowMaximumTrainerSpawnCount(ServerWorld world, ServerPlayerEntity player) {
        int total = SPAWNERS.stream().map(spawner -> spawner.getEntityCount(world, player)).mapToInt(Integer::intValue).sum();
        int maximum = ConfigStorage.getInstance().getMaximumTrainerSpawnCount();
        return total < maximum;
    }

    private TrainerEntitySpawner getRandomSpawner(List<TrainerEntitySpawner> spawners) {
        int total = spawners.stream().map(TrainerEntitySpawner::getWeight).mapToInt(Integer::intValue).sum();
        int random = Random.create().nextInt(total) + 1;

        for (TrainerEntitySpawner spawner : spawners) {
            int weight = spawner.getWeight();
            if (random <= weight) {
                return spawner;
            }
            random -= weight;
        }

        throw new IllegalStateException();
    }

    private List<TrainerEntitySpawner> getSpawners() {
        List<TrainerEntitySpawner> spawners = new ArrayList<>(SPAWNERS);

        if (!ConfigStorage.getInstance().getAllowHostileTrainerSpawn()) {
            spawners = spawners.stream().filter(spawner -> !(spawner instanceof HostileTrainerEntitySpawner)).toList();
        }

        return spawners;
    }
}
