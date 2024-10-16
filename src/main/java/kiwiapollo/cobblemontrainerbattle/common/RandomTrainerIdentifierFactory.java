package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battlefactory.BattleFactory;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomTrainerIdentifierFactory {
    public Identifier create() {
        List<Identifier> identifiers = new ArrayList<>(CobblemonTrainerBattle.trainers.keySet());
        Collections.shuffle(identifiers);

        return identifiers.get(0);
    }

    public Identifier createInNamespace(String namespace) {
        List<Identifier> identifiers = new ArrayList<>(CobblemonTrainerBattle.trainers.keySet().stream()
                .filter(identifier -> identifier.getPath().startsWith(namespace)).toList());
        Collections.shuffle(identifiers);

        return identifiers.get(0);
    }

    public Identifier createForBattleFactory() {
        Identifier identifier = create();

        while (getPokemonCount(identifier) < BattleFactory.POKEMON_COUNT) {
            identifier = create();
        }

        return identifier;
    }

    private int getPokemonCount(Identifier identifier) {
        return CobblemonTrainerBattle.trainers.get(identifier).pokemons().size();
    }
}
