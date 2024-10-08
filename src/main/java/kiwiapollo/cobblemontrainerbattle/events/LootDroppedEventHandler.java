package kiwiapollo.cobblemontrainerbattle.events;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.events.drops.LootDroppedEvent;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battlefactory.BattleFactory;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.EntityBackedTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.groupbattle.GroupBattle;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;

public class LootDroppedEventHandler {
    public void onLootDropped(LootDroppedEvent lootDroppedEvent) {
        if (!(lootDroppedEvent.getEntity() instanceof PokemonEntity)) {
            return;
        }

        PokemonEntity pokemonEntity = (PokemonEntity) lootDroppedEvent.getEntity();

        boolean isGroupBattle = GroupBattle.trainerBattles.values().stream()
                .map(PokemonBattle::getBattleId).toList()
                .contains(pokemonEntity.getBattleId());

        boolean isBattleFactory = BattleFactory.trainerBattles.values().stream()
                .map(PokemonBattle::getBattleId).toList()
                .contains(pokemonEntity.getBattleId());

        boolean isTrainerBattle = TrainerBattle.trainerBattles.values().stream()
                .map(PokemonBattle::getBattleId).toList()
                .contains(pokemonEntity.getBattleId());

        boolean isEntityBackedTrainerBattle = EntityBackedTrainerBattle.trainerBattles.values().stream()
                .map(PokemonBattle::getBattleId).toList()
                .contains(pokemonEntity.getBattleId());

        if (isGroupBattle || isBattleFactory || isTrainerBattle || isEntityBackedTrainerBattle) {
            lootDroppedEvent.cancel();
            CobblemonTrainerBattle.LOGGER.info("Cancelled LOOT_DROPPED event");
        }
    }
}
