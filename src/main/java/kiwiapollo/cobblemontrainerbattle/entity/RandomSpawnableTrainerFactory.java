package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class RandomSpawnableTrainerFactory implements SimpleFactory<String> {
    private final Predicate<String> predicate;

    public RandomSpawnableTrainerFactory(Predicate<String> predicate) {
        this.predicate = predicate;
    }

    @Override
    public String create() {
        List<String> trainers = TrainerStorage.getInstance().keySet().stream().filter(predicate).filter(this::isSpawningAllowed).toList();
        List<String> random = new ArrayList<>(trainers);
        Collections.shuffle(random);

        return random.get(0);
    }

    private boolean isSpawningAllowed(String trainer) {
        try {
            return TrainerStorage.getInstance().get(trainer).isSpawningAllowed();

        } catch (NullPointerException e) {
            return false;
        }
    }
}
