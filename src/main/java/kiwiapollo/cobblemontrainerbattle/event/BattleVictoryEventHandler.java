package kiwiapollo.cobblemontrainerbattle.event;

import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;
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
        if (!isTrainerBattle(event)) {
            return Unit.INSTANCE;
        }

        if (isPlayerVictory(event)) {
            onPlayerVictory(event);

        } else {
            onPlayerDefeat(event);
        }

        return Unit.INSTANCE;
    }

    private boolean isTrainerBattle(BattleVictoryEvent event) {
        try {
            ServerPlayerEntity player = event.getBattle().getPlayers().get(0);
            UUID battleId = BattleContextStorage.getInstance().getOrCreate(player.getUuid()).getTrainerBattle().getBattleId();

            return battleId.equals(event.getBattle().getBattleId());

        } catch (NullPointerException e) {
            return false;
        }
    }

    private boolean isPlayerVictory(BattleVictoryEvent event) {
        ServerPlayerEntity player = event.getBattle().getPlayers().get(0);
        return event.getWinners().stream().anyMatch(battleActor -> battleActor.isForPlayer(player));
    }

    private void onPlayerVictory(BattleVictoryEvent event) {
        ServerPlayerEntity player = event.getBattle().getPlayers().get(0);

        BattleContextStorage.getInstance().getOrCreate(player.getUuid()).getTrainerBattle().onPlayerVictory();

        BattleContextStorage.getInstance().getOrCreate(player.getUuid()).clearTrainerBattle();
    }

    private void onPlayerDefeat(BattleVictoryEvent event) {
        ServerPlayerEntity player = event.getBattle().getPlayers().get(0);

        BattleContextStorage.getInstance().getOrCreate(player.getUuid()).getTrainerBattle().onPlayerDefeat();

        BattleContextStorage.getInstance().getOrCreate(player.getUuid()).clearTrainerBattle();
    }
}
