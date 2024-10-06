package kiwiapollo.cobblemontrainerbattle.events;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.events.drops.LootDroppedEvent;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;

import java.util.List;
import java.util.UUID;

public class LootDroppedEventHandler {
    public void onLootDropped(LootDroppedEvent lootDroppedEvent) {
        if (!(lootDroppedEvent.getEntity() instanceof PokemonEntity)) return;

        PokemonEntity pokemonEntity = (PokemonEntity) lootDroppedEvent.getEntity();
        List<UUID> battleIds = CobblemonTrainerBattle.trainerBattles.values().stream().map(PokemonBattle::getBattleId).toList();
        if (!battleIds.contains(pokemonEntity.getBattleId())) return;

        lootDroppedEvent.cancel();
        CobblemonTrainerBattle.LOGGER.info("Cancelled LOOT_DROPPED event");
    }
}
