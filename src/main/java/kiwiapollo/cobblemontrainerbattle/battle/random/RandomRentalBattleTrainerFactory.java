package kiwiapollo.cobblemontrainerbattle.battle.random;

import kiwiapollo.cobblemontrainerbattle.battle.preset.RentalBattlePreset;
import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomRentalBattleTrainerFactory implements SimpleFactory<String> {
    @Override
    public String create() {
        List<String> trainers = TrainerStorage.getInstance().keySet().stream().filter(this::hasRentalBattlePartySize).toList();
        List<String> random = new ArrayList<>(trainers);
        Collections.shuffle(random);

        return random.get(0);
    }

    private boolean hasRentalBattlePartySize(String trainer) {
        return TrainerStorage.getInstance().get(trainer).getParty().occupied() == RentalBattlePreset.PARTY_SIZE;
    }
}
