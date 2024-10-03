package kiwiapollo.cobblemontrainerbattle.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.groupbattle.GroupBattle;
import net.minecraft.server.command.ServerCommandSource;

public class BattleGroupFlatCommand extends LiteralArgumentBuilder<ServerCommandSource> {

    public BattleGroupFlatCommand() {
        super("battlegroupflat");

        this.requires(new PlayerCommandPredicate(String.format("%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral())))
                .then(getStartSessionAndStartBattleCommand())
                .executes(GroupBattle::startBattleWithFlatLevelAndFullHealthOrStopSession);
    }

    private ArgumentBuilder<ServerCommandSource, ?> getStartSessionAndStartBattleCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("start")
                .then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("group", StringArgumentType.greedyString())
                        .suggests((context, builder) -> {
                            CobblemonTrainerBattle.groupFiles.keySet().stream().map(String::valueOf).forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .executes(GroupBattle::startSessionAndStartBattleWithFlatLevelAndFullHealth));
    }
}
