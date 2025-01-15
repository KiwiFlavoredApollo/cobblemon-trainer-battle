package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import kiwiapollo.cobblemontrainerbattle.parser.preset.TrainerPreset;

import java.util.function.Predicate;

@Deprecated
public class SpawningAllowedPredicate implements Predicate<TrainerPreset> {
    @Override
    public boolean test(TrainerPreset preset) {
        return preset.isSpawningAllowed;
    }
}
