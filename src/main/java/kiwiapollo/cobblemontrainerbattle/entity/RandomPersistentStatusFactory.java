package kiwiapollo.cobblemontrainerbattle.entity;

import com.cobblemon.mod.common.pokemon.status.PersistentStatus;
import com.cobblemon.mod.common.pokemon.status.statuses.persistent.*;
import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomPersistentStatusFactory implements SimpleFactory<PersistentStatus> {
    @Override
    public PersistentStatus create() {
        List<PersistentStatus> random = new ArrayList<>(List.of(
                new ParalysisStatus(),
                new BurnStatus(),
                new FrozenStatus(),
                new SleepStatus(),
                new PoisonStatus(),
                new PoisonBadlyStatus()
        ));
        Collections.shuffle(random);
        return random.get(0);
    }
}
