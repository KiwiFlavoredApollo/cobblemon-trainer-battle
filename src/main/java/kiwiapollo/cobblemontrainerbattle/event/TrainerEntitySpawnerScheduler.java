package kiwiapollo.cobblemontrainerbattle.event;

import kiwiapollo.cobblemontrainerbattle.gamerule.ModGameRule;
import kiwiapollo.cobblemontrainerbattle.item.VsSeeker;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;

import java.util.List;

import static net.minecraft.SharedConstants.TICKS_PER_SECOND;

public class TrainerEntitySpawnerScheduler implements ServerTickEvents.EndWorldTick {
    private static final List<TrainerEntitySpawner> SPAWNERS = List.of(
            new NeutralTrainerEntitySpawner(90)
    );

    @Override
    public void onEndTick(ServerWorld world) {
        if (!isTrainerSpawnTick(world.getServer())) {
            return;
        }

        for (ServerPlayerEntity player : world.getPlayers()) {
            if (!hasVsSeeker(player)) {
                continue;
            }

            if (!isBelowMaximumTrainerSpawnCount(world, player)) {
                continue;
            }

            getRandomSpawner(SPAWNERS).spawnEntity(world, player);
        }
    }

    private boolean isTrainerSpawnTick(MinecraftServer server) {
        int interval = server.getGameRules().get(ModGameRule.TRAINER_SPAWN_INTERVAL_IN_SECONDS).get() * TICKS_PER_SECOND;
        return server.getTicks() % interval == 0;
    }

    private boolean hasVsSeeker(ServerPlayerEntity player) {
        return !VsSeeker.getVsSeekers(player.getInventory()).isEmpty();
    }

    private boolean isBelowMaximumTrainerSpawnCount(ServerWorld world, ServerPlayerEntity player) {
        int total = SPAWNERS.stream().map(spawner -> spawner.getEntityCount(world, player)).mapToInt(Integer::intValue).sum();
        int maximum = world.getServer().getGameRules().get(ModGameRule.MAXIMUM_TRAINER_SPAWN_COUNT).get();
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
}
