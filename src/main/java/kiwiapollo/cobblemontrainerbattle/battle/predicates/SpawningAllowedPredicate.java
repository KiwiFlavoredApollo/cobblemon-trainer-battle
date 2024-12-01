package kiwiapollo.cobblemontrainerbattle.battle.predicates;

import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerProfileStorage;
import net.minecraft.util.Identifier;

import java.util.function.Predicate;

public class SpawningAllowedPredicate implements Predicate<Identifier> {

    @Override
    public boolean test(Identifier trainer) {
        return TrainerProfileStorage.getProfileRegistry().get(trainer).isSpawningAllowed();
    }
}
