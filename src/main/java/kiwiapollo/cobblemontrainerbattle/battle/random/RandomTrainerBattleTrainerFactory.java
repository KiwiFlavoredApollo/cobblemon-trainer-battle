package kiwiapollo.cobblemontrainerbattle.battle.random;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerTemplateStorage;
import net.minecraft.util.Identifier;

import java.util.*;

public class RandomTrainerBattleTrainerFactory implements SimpleFactory<Identifier> {
    @Override
    public Identifier create() {
        List<Identifier> random = new ArrayList<>(TrainerTemplateStorage.getInstance().keySet());
        Collections.shuffle(random);

        return random.get(0);
    }
}
