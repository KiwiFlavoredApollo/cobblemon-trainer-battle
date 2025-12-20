package kiwiapollo.cobblemontrainerbattle.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;
import kiwiapollo.cobblemontrainerbattle.battle.TrainerBattleActor;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class BattleVictoryEventHandler implements Function1<BattleVictoryEvent, Unit> {
    public static void initialize() {
        CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.NORMAL, new BattleVictoryEventHandler());
    }

    /**
     * BATTLE_VICTORY event fires even if the player loses
     */
    @Override
    public Unit invoke(BattleVictoryEvent event) {
        event.getWinners().stream()
                .filter(actor -> actor instanceof TrainerBattleActor)
                .map(actor -> (TrainerBattleActor) actor)
                .forEach(TrainerBattleActor::onPlayerDefeat);

        event.getLosers().stream()
                .filter(actor -> actor instanceof TrainerBattleActor)
                .map(actor -> (TrainerBattleActor) actor)
                .forEach(TrainerBattleActor::onPlayerVictory);

        return Unit.INSTANCE;
    }
}
