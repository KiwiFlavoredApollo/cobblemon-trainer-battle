package kiwiapollo.cobblemontrainerbattle.events;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.events.drops.LootDroppedEvent;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battlefactory.BattleFactory;
import kiwiapollo.cobblemontrainerbattle.groupbattle.GroupBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;

public class LootDroppedEventHandler {
    public void onLootDropped(LootDroppedEvent lootDroppedEvent) {
        if (!(lootDroppedEvent.getEntity() instanceof PokemonEntity)) {
            return;
        }

        PokemonEntity pokemonEntity = (PokemonEntity) lootDroppedEvent.getEntity();
        boolean isTrainerBattle = CobblemonTrainerBattle.trainerBattles.values().stream()
                .map(TrainerBattle::getBattleId)
                .toList().contains(pokemonEntity.getBattleId());

        if (isTrainerBattle) {
            lootDroppedEvent.cancel();
            CobblemonTrainerBattle.LOGGER.info("Cancelled LOOT_DROPPED event");
        }
    }
}
