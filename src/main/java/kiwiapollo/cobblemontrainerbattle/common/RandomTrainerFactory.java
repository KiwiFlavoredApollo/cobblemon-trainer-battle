package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.parser.preset.TrainerStorage;

import java.util.*;

public class RandomTrainerFactory implements SimpleFactory<String> {
    @Override
    public String create() {
        List<String> random = new ArrayList<>(TrainerStorage.getInstance().keySet());
        Collections.shuffle(random);

        return random.get(0);
    }
}
