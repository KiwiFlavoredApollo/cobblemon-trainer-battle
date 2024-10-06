package kiwiapollo.cobblemontrainerbattle.entities;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class TrainerEntityFactory {
    public TrainerEntity create(ServerWorld world, PlayerEntity player, BlockPos spawnPos) {
        TrainerEntity trainerEntity = new TrainerEntity(CobblemonTrainerBattle.TRAINER_ENTITY_TYPE, world);
        trainerEntity.refreshPositionAndAngles(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(),
                player.getYaw(), player.getPitch());

        return trainerEntity;
    }
}
