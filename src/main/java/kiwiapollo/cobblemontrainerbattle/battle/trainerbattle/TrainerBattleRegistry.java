package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrainerBattleRegistry {
    private static final Map<UUID, TrainerBattle> battles = new HashMap<>();

    public TrainerBattle get(UUID uuid) {
        return battles.get(uuid);
    }

    public void put(UUID uuid, TrainerBattle trainerBattle) {
        battles.put(uuid, trainerBattle);
    }

    public boolean containsKey(UUID uuid) {
        return battles.containsKey(uuid);
    }

    public void remove(UUID uuid) {
        battles.remove(uuid);
    }

    public Collection<TrainerBattle> values() {
        return battles.values();
    }
}
