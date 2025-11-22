package kiwiapollo.cobblemontrainerbattle.event;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.CustomTrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContextStorage;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class BattleVictoryEventHandler implements Function1<BattleVictoryEvent, Unit> {
    /**
     * BATTLE_VICTORY event fires even if the player loses
     */
    @Override
    public Unit invoke(BattleVictoryEvent event) {
        event.getWinners().stream()
                .filter(actor -> actor instanceof CustomTrainerBattleActor)
                .map(actor -> (CustomTrainerBattleActor) actor)
                .forEach(CustomTrainerBattleActor::onPlayerDefeat);

        event.getLosers().stream()
                .filter(actor -> actor instanceof CustomTrainerBattleActor)
                .map(actor -> (CustomTrainerBattleActor) actor)
                .forEach(CustomTrainerBattleActor::onPlayerVictory);

        return Unit.INSTANCE;
    }
}
