package kiwiapollo.cobblemontrainerbattle.parser.player;

import kiwiapollo.cobblemontrainerbattle.common.LazyMap;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BattleContextStorage implements LazyMap<UUID, BattleContext> {
    static BattleContextStorage instance;

    private Map<UUID, BattleContext> storage;

    public static BattleContextStorage getInstance() {
        if (instance == null) {
            instance = new BattleContextStorage();
        }

        return instance;
    }

    private BattleContextStorage() {
        this.storage = new HashMap<>();
    }

    public BattleContext getOrCreate(UUID uuid) {
        if (!storage.containsKey(uuid)) {
            storage.put(uuid, new BattleContext(uuid));
        }

        return storage.get(uuid);
    }

    @Override
    public void put(UUID uuid, BattleContext context) {
        storage.put(uuid, context);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public void remove(UUID uuid) {
        storage.remove(uuid);
    }

    @Override
    public Iterable<? extends Map.Entry<UUID, BattleContext>> entrySet() {
        return storage.entrySet();
    }
}
