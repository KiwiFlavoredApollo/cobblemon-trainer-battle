package kiwiapollo.cobblemontrainerbattle.battle.groupbattle;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;

public class NormalGroupBattle {
    public static int startSession(CommandContext<ServerCommandSource> context) {
        return GroupBattle.startSession(context, NormalGroupBattleSession::new);
    }

    public static int stopSession(CommandContext<ServerCommandSource> context) {
        return GroupBattle.stopSession(context);
    }

    public static int startBattle(CommandContext<ServerCommandSource> context) {
        return GroupBattle.startBattle(context);
    }
}
