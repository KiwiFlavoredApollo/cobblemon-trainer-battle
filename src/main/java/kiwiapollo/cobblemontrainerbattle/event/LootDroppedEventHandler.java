package kiwiapollo.cobblemontrainerbattle.event;

import com.cobblemon.mod.common.api.events.drops.LootDroppedEvent;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattleStorage;
import kotlin.Unit;

public class LootDroppedEventHandler {
    public static Unit onLootDropped(LootDroppedEvent lootDroppedEvent) {
        // LOOT_DROPPED event fires before BATTLE_VICTORY event
        // Cobblemon Discord, Hiroku: It's only used if the player kills the pokemon by hand, not by battle
        // However Pokemons drop loot when defeated in battles, at least on 1.5.1

        if (!(lootDroppedEvent.getEntity() instanceof PokemonEntity)) {
            return null;
        }

        PokemonEntity pokemonEntity = (PokemonEntity) lootDroppedEvent.getEntity();
        boolean isTrainerBattle = TrainerBattleStorage.values().stream()
                .map(TrainerBattle::getBattleId)
                .toList().contains(pokemonEntity.getBattleId());

        if (isTrainerBattle) {
            lootDroppedEvent.cancel();
            CobblemonTrainerBattle.LOGGER.info("Cancelled LOOT_DROPPED event");
        }

        return Unit.INSTANCE;
    }
}
