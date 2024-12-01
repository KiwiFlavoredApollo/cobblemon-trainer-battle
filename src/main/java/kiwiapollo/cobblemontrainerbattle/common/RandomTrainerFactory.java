package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerProfileStorage;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RandomTrainerFactory implements SimpleFactory<Identifier> {
    private final Set<Identifier> trainers;

    private RandomTrainerFactory(Set<Identifier> trainers) {
        this.trainers = trainers;
    }

    @Override public Identifier create() {
        List<Identifier> random = new ArrayList<>(trainers);
        Collections.shuffle(random);

        return random.get(0);
    }
    
    public static class Builder {
        private String RADICALRED = "radicalred";
        private String INCLEMENTEMERALD = "inclementemerald";
        private String SMOGON = "smogon";
        
        private Set<Identifier> trainers;
        private List<Predicate<Identifier>> predicates;

        public Builder() {
            this.trainers = new HashSet<>();
            this.predicates = new ArrayList<>();
        }

        public RandomTrainerFactory build() {
            Set<Identifier> filtered = trainers.stream()
                    .filter(identifier -> predicates.stream().allMatch(predicate -> predicate.test(identifier)))
                    .collect(Collectors.toSet());

            return new RandomTrainerFactory(filtered);
        }

        public Builder addPredicate(Predicate<Identifier> predicate) {
            predicates.add(predicate);
            return this;
        }
        
        public Builder addAllTrainers() {
            Set<Identifier> all = TrainerProfileStorage.getProfileRegistry().keySet();
            trainers.addAll(all);
            return this;
        }
        
        public Builder addRadicalRedTrainers() {
            Set<Identifier> all = TrainerProfileStorage.getProfileRegistry().keySet();
            trainers.addAll(all.stream().filter(this::isRadicalRedTrainer).collect(Collectors.toSet()));
            return this;
        }
        
        public Builder addInclementEmeraldTrainers() {
            Set<Identifier> all = TrainerProfileStorage.getProfileRegistry().keySet();
            trainers.addAll(all.stream().filter(this::isInclementEmeraldTrainer).collect(Collectors.toSet()));
            return this;
        }
        
        public Builder addSmogonTrainers() {
            Set<Identifier> all = TrainerProfileStorage.getProfileRegistry().keySet();
            trainers.addAll(all.stream().filter(this::isSmogonTrainer).collect(Collectors.toSet()));
            return this;
        }
        
        private boolean isRadicalRedTrainer(Identifier trainer) {
            return trainer.getPath().startsWith(RADICALRED);
        }

        private boolean isInclementEmeraldTrainer(Identifier trainer) {
            return trainer.getPath().startsWith(INCLEMENTEMERALD);
        }

        private boolean isSmogonTrainer(Identifier trainer) {
            return trainer.getPath().startsWith(SMOGON);
        }
    }
}
