package kiwiapollo.cobblemontrainerbattle.global.history;

import kiwiapollo.cobblemontrainerbattle.common.LazyMap;

import java.util.*;

public class PlayerHistoryStorage implements LazyMap<UUID, PlayerHistory> {
    private static PlayerHistoryStorage instance;
    private static Map<UUID, PlayerHistory> storage;

    private PlayerHistoryStorage() {
        storage = new HashMap<>();
    }

    public static PlayerHistoryStorage getInstance() {
        if (instance == null) {
            instance = new PlayerHistoryStorage();
        }

        return instance;
    }

    @Override
    public PlayerHistory getOrCreate(UUID uuid) {
        if (!storage.containsKey(uuid)) {
            storage.put(uuid, new PlayerHistory());
        }

        return storage.get(uuid);
    }

    @Override
    public void put(UUID uuid, PlayerHistory history) {
        storage.put(uuid, history);
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
    public Iterable<? extends Map.Entry<UUID, PlayerHistory>> entrySet() {
        return storage.entrySet();
    }
}
