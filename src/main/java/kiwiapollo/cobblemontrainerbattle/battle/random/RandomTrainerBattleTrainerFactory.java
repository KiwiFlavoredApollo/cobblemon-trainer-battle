package kiwiapollo.cobblemontrainerbattle.battle.random;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerStorage;

import java.util.*;

public class RandomTrainerBattleTrainerFactory implements SimpleFactory<String> {
    @Override
    public String create() {
        List<String> random = new ArrayList<>(TrainerStorage.getInstance().keySet());
        Collections.shuffle(random);

        return random.get(0);
    }
}
