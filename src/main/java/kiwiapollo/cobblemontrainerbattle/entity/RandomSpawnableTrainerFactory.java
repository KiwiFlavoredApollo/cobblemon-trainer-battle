package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class RandomSpawnableTrainerFactory implements SimpleFactory<String> {
    private final Predicate<String> predicate;

    public RandomSpawnableTrainerFactory() {
        this(t -> true);
    }

    private RandomSpawnableTrainerFactory(Predicate<String> predicate) {
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

    public static class Builder {
        private final List<Predicate<String>> trainers;

        public Builder() {
            this.trainers = new ArrayList<>(List.of(t -> false));
        }

        public RandomSpawnableTrainerFactory build() {
            return new RandomSpawnableTrainerFactory(toPredicate());
        }

        private Predicate<String> toPredicate() {
            return this.trainers.stream().reduce(Predicate::or).orElse(t -> true);
        }

        public Builder addAll() {
            trainers.add(t -> true);
            return this;
        }

        public Builder addRadicalRed() {
            trainers.add(t -> t.matches("^radicalred/.+"));
            return this;
        }

        public Builder addInclementEmerald() {
            trainers.add(t -> t.matches("^inclementemerald/.+"));
            return this;
        }

        public Builder addSmogon() {
            trainers.add(t -> t.matches("^smogon/.+"));
            return this;
        }

        public Builder addXy() {
            trainers.add(t -> t.matches("^xy/.+"));
            return this;
        }

        public Builder addBdsp() {
            trainers.add(t -> t.matches("^bdsp/.+"));
            return this;
        }
    }
}
