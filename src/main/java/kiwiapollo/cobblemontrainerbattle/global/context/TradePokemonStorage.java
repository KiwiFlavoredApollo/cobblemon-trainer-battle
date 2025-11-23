package kiwiapollo.cobblemontrainerbattle.global.context;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TradePokemonStorage {
    private static TradePokemonStorage instance;

    private final Map<UUID, TradePokemon> storage;

    private TradePokemonStorage() {
        instance = null;
        storage = new HashMap<>();
    }

    public static TradePokemonStorage getInstance() {
        if (instance == null) {
            instance = new TradePokemonStorage();
        }

        return instance;
    }

    public TradePokemon get(ServerPlayerEntity player) {
        if (!storage.containsKey(player.getUuid())) {
            storage.put(player.getUuid(), new TradePokemon());
        }

        return storage.get(player.getUuid());
    }
}
