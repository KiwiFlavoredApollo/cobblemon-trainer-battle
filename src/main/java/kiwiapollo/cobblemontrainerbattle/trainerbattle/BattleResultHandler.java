package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import com.mojang.brigadier.CommandDispatcher;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.ResultAction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class BattleResultHandler implements ResultHandler {
    private final ServerPlayerEntity player;
    private final ResultAction victory;
    private final ResultAction defeat;

    public BattleResultHandler(ServerPlayerEntity player, ResultAction victory, ResultAction defeat) {
        this.player = player;
        this.victory = victory;
        this.defeat = defeat;
    }

    public void onVictory() {
        CobblemonTrainerBattle.economy.addBalance(player, victory.balance);
        victory.commands.forEach(this::executeCommand);
    }

    public void onDefeat() {
        CobblemonTrainerBattle.economy.removeBalance(player, defeat.balance);
        defeat.commands.forEach(this::executeCommand);
    }

    private void executeCommand(String command) {
        command = command.replace("%player%", player.getGameProfile().getName());

        MinecraftServer server = player.getCommandSource().getServer();
        CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();

        server.getCommandManager().execute(dispatcher.parse(command, server.getCommandSource()), command);
    }
}
