package kiwiapollo.cobblemontrainerbattle.event;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.entity.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;

import java.util.function.Predicate;

public class NormalTrainerEntitySpawner extends TrainerEntitySpawner {
    private final int weight;

    public NormalTrainerEntitySpawner(int weight) {
        super();
        this.weight = weight;
    }

    @Override
    protected TrainerEntity createTrainerEntity(ServerWorld world, ServerPlayerEntity player) {
        Predicate<String> predicate = toPredicate(player.getInventory());
        SimpleFactory<String> trainer = new RandomSpawnableTrainerFactory(predicate);
        return new RandomNormalTrainerEntityFactory(trainer).create(EntityType.NORMAL_TRAINER, world);
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public int getEntityCount(ServerWorld world, ServerPlayerEntity player) {
        Box box = player.getBoundingBox().expand(MAXIMUM_RADIUS);
        return world.getEntitiesByType(EntityType.NORMAL_TRAINER, box, entity -> true).size();
    }
}
