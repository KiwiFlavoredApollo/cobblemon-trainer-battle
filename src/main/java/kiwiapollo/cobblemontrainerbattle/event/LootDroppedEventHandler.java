package kiwiapollo.cobblemontrainerbattle.event;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.events.drops.LootDroppedEvent;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.CustomTrainerBattleActor;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

public class LootDroppedEventHandler implements Function1<LootDroppedEvent, Unit> {
    /**
     * LOOT_DROPPED event fires before BATTLE_VICTORY event.
     * Cobblemon Discord, Hiroku said: It's only used if the player kills the pokemon by hand, not by battle.
     * However, Pokémon drop loots when defeated in battles, at least on 1.5.1
     */
    @Override
    public Unit invoke(LootDroppedEvent event) {
        if (isTrainerBattle(event)) {
            event.cancel();
        }

        return Unit.INSTANCE;
    }

    private boolean isTrainerBattle(LootDroppedEvent event) {
        try {
            UUID battleId = ((PokemonEntity) event.getEntity()).getBattleId();
            PokemonBattle battle = Cobblemon.INSTANCE.getBattleRegistry().getBattle(battleId);
            List<BattleActor> actors = StreamSupport.stream(battle.getActors().spliterator(), false).toList();
            return actors.stream().anyMatch(actor -> actor instanceof CustomTrainerBattleActor);

        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
    }
}
