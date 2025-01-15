package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.parser.preset.TrainerStorage;

import java.util.*;

public class RandomTrainerFactory implements SimpleFactory<String> {
    private final List<String> trainers;

    public RandomTrainerFactory() {
        this(TrainerStorage.getInstance().keySet().stream().toList());
    }

    private RandomTrainerFactory(List<String> trainers) {
        this.trainers = trainers;
    }

    @Override
    public String create() {
        List<String> random = new ArrayList<>(trainers);
        Collections.shuffle(random);

        return random.get(0);
    }

    public static class Builder {
        private List<String> trainers;

        public Builder() {
            this.trainers = new ArrayList<>();
        }

        public RandomTrainerFactory build() {
            return new RandomTrainerFactory(trainers.stream().distinct().toList());
        }

        public Builder addAll() {
            trainers.addAll(TrainerStorage.getInstance().keySet().stream().toList());
            return this;
        }

        public Builder addRadicalRed() {
            trainers.addAll(TrainerStorage.getInstance().keySet().stream().filter(trainer -> trainer.matches("^radicalred/")).toList());
            return this;
        }

        public Builder addInclementEmerald() {
            trainers.addAll(TrainerStorage.getInstance().keySet().stream().filter(trainer -> trainer.matches("^inclementemerald/")).toList());
            return this;
        }

        public Builder addSmogon() {
            trainers.addAll(TrainerStorage.getInstance().keySet().stream().filter(trainer -> trainer.matches("^smogon/")).toList());
            return this;
        }
    }
}
