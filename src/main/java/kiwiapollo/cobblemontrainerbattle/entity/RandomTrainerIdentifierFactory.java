package kiwiapollo.cobblemontrainerbattle.entity;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.preset.TrainerTemplate;
import kiwiapollo.cobblemontrainerbattle.preset.TrainerTemplateStorage;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class RandomTrainerIdentifierFactory implements SimpleFactory<Identifier> {
    private final Predicate<TrainerTemplate> predicate;

    public RandomTrainerIdentifierFactory(Predicate<TrainerTemplate> predicate) {
        this.predicate = predicate;
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
