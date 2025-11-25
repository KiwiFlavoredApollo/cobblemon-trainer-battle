package kiwiapollo.cobblemontrainerbattle.battle;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RentalPokemonStorage {
    private static RentalPokemonStorage instance;

    private final Map<UUID, RentalPokemon> storage;

    private RentalPokemonStorage() {
        instance = null;
        storage = new HashMap<>();
    }

    public static RentalPokemonStorage getInstance() {
        if (instance == null) {
            instance = new RentalPokemonStorage();
        }

        return instance;
    }

    public RentalPokemon get(ServerPlayerEntity player) {
        if (!storage.containsKey(player.getUuid())) {
            storage.put(player.getUuid(), new RentalPokemon());
        }

        return storage.get(player.getUuid());
    }
}
