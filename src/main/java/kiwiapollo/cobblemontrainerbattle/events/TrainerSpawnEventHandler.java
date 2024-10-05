package kiwiapollo.cobblemontrainerbattle.events;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.npc.TrainerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.List;

public class TrainerSpawnEventHandler {
    private static final int SPAWN_INTERVAL = 100;
    private static int tickCounter = 0;

    public static void onEndWorldTick(ServerWorld world) {
        tickCounter++;

        if (tickCounter >= SPAWN_INTERVAL) {
            spawnEntitiesAroundPlayers(world);
            tickCounter = 0;
        }
    }

    private static void spawnEntitiesAroundPlayers(ServerWorld world) {
        List<ServerPlayerEntity> players = world.getPlayers();

        for (PlayerEntity player : players) {
            BlockPos playerPos = player.getBlockPos();
            int radius = 10;
            int xOffset = (int) (Math.random() * radius * 2) - radius;
            int zOffset = (int) (Math.random() * radius * 2) - radius;
            BlockPos spawnPos = playerPos.add(xOffset, 0, zOffset);

            int trainerCount = world.getEntitiesByType(CobblemonTrainerBattle.TRAINER, player.getBoundingBox().expand(radius), entity -> true).size();
            if (trainerCount > 3) {
                continue;
            }

            TrainerEntity trainer = new TrainerEntity(CobblemonTrainerBattle.TRAINER, world);

            if (world.isSpaceEmpty(trainer, new Box(spawnPos))) {
                trainer.refreshPositionAndAngles(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), player.getYaw(), player.getPitch());
                world.spawnEntity(trainer);
                CobblemonTrainerBattle.LOGGER.info("Spawned trainer");
            }
        }
    }
}
