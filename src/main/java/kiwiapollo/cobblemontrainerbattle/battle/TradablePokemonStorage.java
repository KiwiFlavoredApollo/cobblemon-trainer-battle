package kiwiapollo.cobblemontrainerbattle.battle;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TradablePokemonStorage {
    private static TradablePokemonStorage instance;

    private final Map<UUID, TradablePokemon> storage;

    private TradablePokemonStorage() {
        instance = null;
        storage = new HashMap<>();
    }

    public static TradablePokemonStorage getInstance() {
        if (instance == null) {
            instance = new TradablePokemonStorage();
        }

        return instance;
    }

    public TradablePokemon get(ServerPlayerEntity player) {
        if (!storage.containsKey(player.getUuid())) {
            storage.put(player.getUuid(), new TradablePokemon());
        }

        return storage.get(player.getUuid());
    }
}
