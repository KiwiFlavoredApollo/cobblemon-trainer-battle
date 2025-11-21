package kiwiapollo.cobblemontrainerbattle.battle.random;

import kiwiapollo.cobblemontrainerbattle.battle.preset.RentalBattlePreset;
import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerTemplateStorage;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomRentalBattleTrainerFactory implements SimpleFactory<Identifier> {
    @Override
    public Identifier create() {
        List<Identifier> trainers = TrainerTemplateStorage.getInstance().keySet().stream().filter(this::hasRentalBattlePartySize).toList();
        List<Identifier> random = new ArrayList<>(trainers);
        Collections.shuffle(random);

        return random.get(0);
    }

    private boolean hasRentalBattlePartySize(Identifier trainer) {
        return TrainerTemplateStorage.getInstance().get(trainer).getTeam().size() >= RentalBattlePreset.PARTY_SIZE;
    }
}
