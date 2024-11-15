package kiwiapollo.cobblemontrainerbattle.battle.postbattle;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.server.network.ServerPlayerEntity;

public class VictoryActionSetHandler implements Runnable {
    private final ServerPlayerEntity player;
    private final PostBattleActionSet onVictory;

    public VictoryActionSetHandler(ServerPlayerEntity player, PostBattleActionSet onVictory) {
        this.player = player;
        this.onVictory = onVictory;
    }

    @Override
    public void run() {
        CobblemonTrainerBattle.economy.addBalance(player, onVictory.balance);

        onVictory.commands.forEach(command -> new CommandExecutor().accept(player, command));
    }
}
