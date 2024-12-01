package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.battle.battlefactory.BattleFactorySession;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.MinimumPartySizePredicate;
import net.minecraft.util.Identifier;

public class BattleFactoryRandomTrainerFactory implements SimpleFactory<Identifier> {
    private final SimpleFactory<Identifier> factory;

    public BattleFactoryRandomTrainerFactory() {
        this.factory = new RandomTrainerFactory.Builder()
                .addAllTrainers()
                .addPredicate(new MinimumPartySizePredicate.TrainerPredicate(BattleFactorySession.POKEMON_COUNT))
                .build();
    }
    @Override
    public Identifier create() {
        return factory.create();
    }
}
