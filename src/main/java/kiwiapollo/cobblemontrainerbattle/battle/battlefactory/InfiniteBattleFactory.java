package kiwiapollo.cobblemontrainerbattle.battle.battlefactory;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;

public class InfiniteBattleFactory {
    public static int startSession(CommandContext<ServerCommandSource> context) {
        return BattleFactory.startSession(context, InfiniteBattleFactorySession::new);
    }
}
