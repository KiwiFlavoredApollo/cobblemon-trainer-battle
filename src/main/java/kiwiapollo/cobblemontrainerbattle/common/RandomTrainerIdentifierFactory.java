package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.battlefactory.BattleFactory;
import kiwiapollo.cobblemontrainerbattle.parser.ProfileRegistries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomTrainerIdentifierFactory {
    public Identifier create() {
        List<Identifier> identifiers = new ArrayList<>(ProfileRegistries.trainer.keySet());
        Collections.shuffle(identifiers);

        return identifiers.get(0);
    }

    public Identifier create(String regex) {
        List<Identifier> identifiers = new ArrayList<>(ProfileRegistries.trainer.keySet().stream()
                .filter(identifier -> identifier.getPath().matches(regex)).toList());
        Collections.shuffle(identifiers);

        return identifiers.get(0);
    }

    public Identifier createSpawningAllowed() {
        List<Identifier> identifiers = new ArrayList<>(ProfileRegistries.trainer.keySet().stream()
                .filter(this::isSpawningAllowedTrainer).toList());
        Collections.shuffle(identifiers);

        return identifiers.get(0);
    }

    private boolean isSpawningAllowedTrainer(Identifier trainer) {
        return ProfileRegistries.trainer.get(trainer).isSpawningAllowed();
    }

    public Identifier createForBattleFactory() {
        Identifier identifier = create();

        while (getPokemonCount(identifier) < BattleFactory.POKEMON_COUNT) {
            identifier = create();
        }

        return identifier;
    }

    private int getPokemonCount(Identifier identifier) {
        return ProfileRegistries.trainer.get(identifier).team().size();
    }
}
