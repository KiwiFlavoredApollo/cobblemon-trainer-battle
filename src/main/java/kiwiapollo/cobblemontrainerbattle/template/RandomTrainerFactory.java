package kiwiapollo.cobblemontrainerbattle.template;

import kiwiapollo.cobblemontrainerbattle.common.TrainerFactory;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class RandomTrainerFactory implements TrainerFactory {
    private final Predicate<TrainerTemplate> predicate;

    public RandomTrainerFactory(Predicate<TrainerTemplate> predicate) {
        this.predicate = predicate;
    }

    public RandomTrainerFactory() {
        this(template -> true);
    }

    @Override
    public Identifier create() {
        List<Identifier> trainers = TrainerTemplateStorage.getInstance().keySet().stream().filter(this::test).toList();
        List<Identifier> random = new ArrayList<>(trainers);
        Collections.shuffle(random);

        return random.get(0);
    }

    private boolean test(Identifier identifier) {
        return predicate.test(TrainerTemplateStorage.getInstance().get(identifier));
    }
}
