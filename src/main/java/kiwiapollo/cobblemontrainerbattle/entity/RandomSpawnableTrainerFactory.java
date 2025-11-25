package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.preset.TrainerTemplateStorage;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class RandomSpawnableTrainerFactory implements SimpleFactory<Identifier> {
    private final Predicate<Identifier> predicate;

    public RandomSpawnableTrainerFactory(Predicate<Identifier> predicate) {
        this.predicate = predicate;
    }

    @Override
    public Identifier create() {
        List<Identifier> trainers = TrainerTemplateStorage.getInstance().keySet().stream().filter(predicate).filter(this::isSpawningAllowed).toList();
        List<Identifier> random = new ArrayList<>(trainers);
        Collections.shuffle(random);

        return random.get(0);
    }

    private boolean isSpawningAllowed(Identifier trainer) {
        try {
            return TrainerTemplateStorage.getInstance().get(trainer).isSpawningAllowed();

        } catch (NullPointerException e) {
            return false;
        }
    }
}
