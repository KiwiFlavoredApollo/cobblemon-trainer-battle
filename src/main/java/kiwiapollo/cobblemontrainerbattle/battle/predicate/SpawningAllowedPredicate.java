package kiwiapollo.cobblemontrainerbattle.battle.predicate;

import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerPreset;

import java.util.function.Predicate;

@Deprecated
public class SpawningAllowedPredicate implements Predicate<TrainerPreset> {
    @Override
    public boolean test(TrainerPreset preset) {
        return preset.isSpawningAllowed;
    }
}
