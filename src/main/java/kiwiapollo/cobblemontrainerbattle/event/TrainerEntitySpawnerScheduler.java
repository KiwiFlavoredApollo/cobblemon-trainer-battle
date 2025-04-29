package kiwiapollo.cobblemontrainerbattle.event;

import kiwiapollo.cobblemontrainerbattle.global.config.ConfigStorage;
import kiwiapollo.cobblemontrainerbattle.item.VsSeeker;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.random.Random;

import java.util.List;

import static net.minecraft.SharedConstants.TICKS_PER_SECOND;

public class TrainerEntitySpawnerScheduler implements ServerTickEvents.EndWorldTick {
    private static final List<TrainerEntitySpawner> SPAWNERS = List.of(
            new NormalTrainerEntitySpawner(80),
            new HostileTrainerEntitySpawner(20)
    );

    @Override
    public void onEndTick(ServerWorld world) {
        if (!isTrainerSpawnTick(world.getServer().getTicks())) {
            return;
        }

        for (ServerPlayerEntity player : world.getPlayers()) {
            if (getVsSeekers(player.getInventory()).isEmpty()) {
                continue;
            }

            if (!isBelowMaximumTrainerSpawnCount(world, player)) {
                continue;
            }

            choose(SPAWNERS).spawnEntity(world, player);
        }
    }

    private boolean isTrainerSpawnTick(int ticks) {
        int interval = ConfigStorage.getInstance().getTrainerSpawnIntervalInSeconds() * TICKS_PER_SECOND;
        return ticks % interval == 0;
    }

    private List<VsSeeker> getVsSeekers(PlayerInventory inventory) {
        return inventory.combinedInventory.stream()
                .flatMap(DefaultedList::stream)
                .filter(stack -> !stack.isEmpty())
                .map(ItemStack::getItem)
                .filter(item -> item instanceof VsSeeker)
                .map(item -> (VsSeeker) item).toList();
    }

    private boolean isBelowMaximumTrainerSpawnCount(ServerWorld world, ServerPlayerEntity player) {
        int count = SPAWNERS.stream()
                .map(spawner -> spawner.getEntityCount(world, player))
                .mapToInt(Integer::intValue)
                .sum();
        return count < ConfigStorage.getInstance().getMaximumTrainerSpawnCount();
    }

    private TrainerEntitySpawner choose(List<TrainerEntitySpawner> spawners) {
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
