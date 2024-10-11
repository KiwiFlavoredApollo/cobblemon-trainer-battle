package kiwiapollo.cobblemontrainerbattle.battlefactory;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.RandomTrainerIdentifierFactory;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;
import net.minecraft.util.Identifier;

public class BattleFactoryRandomTrainerIdentifierFactory {
    public Identifier create() {
        RandomTrainerIdentifierFactory factory = new RandomTrainerIdentifierFactory();
        Identifier identifier = factory.create();
        Trainer trainer = CobblemonTrainerBattle.trainers.get(identifier);

        while (trainer.pokemons.size() < BattleFactory.POKEMON_COUNT) {
            identifier = factory.create();
            trainer = CobblemonTrainerBattle.trainers.get(identifier);
        }

        return identifier;
    }
}
