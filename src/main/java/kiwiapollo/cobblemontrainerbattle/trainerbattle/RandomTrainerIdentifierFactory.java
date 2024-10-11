package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
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

    public Identifier create(String namespace) {
        List<Identifier> identifiers = new ArrayList<>(CobblemonTrainerBattle.trainers.keySet().stream()
                .filter(identifier -> identifier.getPath().startsWith(namespace)).toList());
        Collections.shuffle(identifiers);

        return identifiers.get(0);
    }
}
