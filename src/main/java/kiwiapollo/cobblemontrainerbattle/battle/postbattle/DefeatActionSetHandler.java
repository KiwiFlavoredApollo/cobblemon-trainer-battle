package kiwiapollo.cobblemontrainerbattle.battle.postbattle;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.server.network.ServerPlayerEntity;

public class DefeatActionSetHandler implements Runnable {
    private final ServerPlayerEntity player;
    private final PostBattleActionSet onDefeat;

    public DefeatActionSetHandler(ServerPlayerEntity player, PostBattleActionSet onDefeat) {
        this.player = player;
        this.onDefeat = onDefeat;
    }

    @Override
    public void run() {
        CobblemonTrainerBattle.economy.addBalance(player, onDefeat.balance);

        onDefeat.commands.forEach(command -> new CommandExecutor().accept(player, command));
    }
}
