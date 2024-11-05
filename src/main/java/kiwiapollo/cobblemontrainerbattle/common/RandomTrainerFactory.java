package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.parser.TrainerProfileStorage;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class RandomTrainerFactory implements IdentifierFactory {
    private final Predicate<Identifier> predicate;

    public RandomTrainerFactory() {
        this(identifier -> true);
    }

    public RandomTrainerFactory(Predicate<Identifier> predicate) {
        this.predicate = predicate;
    }

    @Override public Identifier create() {
        List<Identifier> trainers = new ArrayList<>(TrainerProfileStorage.keySet().stream()
                .filter(predicate).toList());
        Collections.shuffle(trainers);

        return trainers.get(0);
    }
}
