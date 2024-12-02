package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.battle.predicates.TrainerRegexPredicate;
import net.minecraft.util.Identifier;

public class GlobalRandomTrainerFactory implements SimpleFactory<Identifier> {
    private final RandomTrainerFactory factory;

    public GlobalRandomTrainerFactory() {
        this.factory = new RandomTrainerFactory.Builder()
                .filter(new TrainerRegexPredicate.Builder().addWildcard().build())
                .build();
    }

    @Override
    public Identifier create() {
        return factory.create();
    }
}
