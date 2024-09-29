package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.Random;

public class RandomTrainerFactory {
    public Trainer create(ServerPlayerEntity player) {
        List<Trainer> trainers = List.of(
                new RadicalRedRandomTrainerFactory().create(player),
                new InclementEmeraldRandomTrainerFactory().create(player)
        );

        int random = new Random().nextInt(trainers.size());
        return trainers.get(random);
    }
}
