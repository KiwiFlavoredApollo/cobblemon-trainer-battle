package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.exceptions.CreateTrainerFailedException;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TotalRandomTrainerFactory {
    public Trainer create(ServerPlayerEntity player) {
        List<RandomTrainerFactory> randomTrainerFactories = List.of(
                new RadicalRedRandomTrainerFactory(),
                new InclementEmeraldRandomTrainerFactory(),
                new CustomRandomTrainerFactory()
        );

        List<Trainer> trainers = new ArrayList<>();
        for (RandomTrainerFactory randomTrainerFactory : randomTrainerFactories) {
            try {
                trainers.add(randomTrainerFactory.create(player));
            } catch (CreateTrainerFailedException ignored) {

            }
        }

        int random = new Random().nextInt(trainers.size());
        return trainers.get(random);
    }
}
