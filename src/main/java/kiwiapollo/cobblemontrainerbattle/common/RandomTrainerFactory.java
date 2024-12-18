package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerProfileStorage;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.function.Predicate;

public class RandomTrainerFactory implements SimpleFactory<Identifier> {
    private final Predicate<Identifier> predicate;

    private RandomTrainerFactory(Predicate<Identifier> predicate) {
        this.predicate = predicate;
    }

    @Override
    public Identifier create() {
        List<Identifier> trainers = TrainerProfileStorage.getProfileRegistry().keySet().stream().filter(predicate).toList();
        List<Identifier> random = new ArrayList<>(trainers);
        Collections.shuffle(random);

        return random.get(0);
    }

    public static class Builder {
        private Predicate<Identifier> predicate;

        public Builder() {
            this.predicate = trainer -> true;
        }

        public RandomTrainerFactory build() {
            return new RandomTrainerFactory(predicate);
        }

        public Builder filter(Predicate<Identifier> predicate) {
            this.predicate = this.predicate.and(predicate);
            return this;
        }
    }
}
