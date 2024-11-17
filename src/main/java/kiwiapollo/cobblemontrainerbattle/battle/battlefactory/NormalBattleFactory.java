package kiwiapollo.cobblemontrainerbattle.battle.battlefactory;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;

public class NormalBattleFactory {
    public static int startSession(CommandContext<ServerCommandSource> context) {
        return BattleFactory.startSession(context, NormalBattleFactorySession::new);
    }
}
