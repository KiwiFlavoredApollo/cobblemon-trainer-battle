package kiwiapollo.trainerbattle.events;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.events.drops.LootDroppedEvent;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import kiwiapollo.trainerbattle.TrainerBattle;

import java.util.List;
import java.util.UUID;

public class LootDroppedEventHandler {
    public void run(LootDroppedEvent lootDroppedEvent) {
        if (!(lootDroppedEvent.getEntity() instanceof PokemonEntity)) return;

        PokemonEntity pokemonEntity = (PokemonEntity) lootDroppedEvent.getEntity();
        List<UUID> battleIds = TrainerBattle.TRAINER_BATTLES.stream().map(PokemonBattle::getBattleId).toList();
        if (!battleIds.contains(pokemonEntity.getBattleId())) return;

        lootDroppedEvent.cancel();
        TrainerBattle.LOGGER.info("Cancelled LOOT_DROPPED event");
    }
}
