package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import java.util.List;
import java.util.Random;

public class RandomTrainerFactory {
    public Trainer create() {
        List<Trainer> trainers = List.of(
                new RadicalRedRandomTrainerFactory().create(),
                new InclementEmeraldRandomTrainerFactory().create()
        );

        int random = new Random().nextInt(trainers.size());
        return trainers.get(random);
    }
}
