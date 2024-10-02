package kiwiapollo.cobblemontrainerbattle.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battlegroup.GroupBattle;
import kiwiapollo.cobblemontrainerbattle.exceptions.ExecuteCommandFaildException;
import net.minecraft.server.command.ServerCommandSource;

public class BattleGroupFlatCommand extends LiteralArgumentBuilder<ServerCommandSource> {

    public BattleGroupFlatCommand() {
        super("battlegroupflat");

        this.requires(new PlayerCommandPredicate(String.format("%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral())))
                .then(getStartSessionCommand())
                .then(getStopSessionCommand())
                .then(getStartBattleCommand());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getStartSessionCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("start")
                .then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("group", StringArgumentType.greedyString())
                        .suggests((context, builder) -> {
                            CobblemonTrainerBattle.groupFiles.keySet().stream().map(String::valueOf).forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            try {
                                GroupBattle.startSession(context);
                                GroupBattle.battleGroupWithFlatLevelAndFullHealth(context);
                                return Command.SINGLE_SUCCESS;

                            } catch (ExecuteCommandFaildException e) {
                                return -1;
                            }
                        }));
    }

    private ArgumentBuilder<ServerCommandSource, ?> getStopSessionCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("stop")
                .executes(context -> {
                    try {
                        GroupBattle.stopSession(context);
                        return Command.SINGLE_SUCCESS;

                    } catch (ExecuteCommandFaildException e) {
                        return -1;
                    }
                });
    }

    private ArgumentBuilder<ServerCommandSource, ?> getStartBattleCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("battle")
                .executes(context -> {
                    try {
                        GroupBattle.battleGroupWithFlatLevelAndFullHealth(context);
                        return Command.SINGLE_SUCCESS;

                    } catch (ExecuteCommandFaildException e) {
                        return -1;
                    }
                });
    }
}
