package kiwiapollo.cobblemontrainerbattle.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battlegroup.BattleGroup;
import kiwiapollo.cobblemontrainerbattle.exceptions.ExecuteCommandFaildException;
import net.minecraft.server.command.ServerCommandSource;

public class BattleGroupCommand extends LiteralArgumentBuilder<ServerCommandSource> {

    public BattleGroupCommand() {
        super("battlegroup");

        this.requires(new PlayerCommandPredicate(String.format("%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral())))
                .then(getBattleGroupStartCommand())
                .then(getBattleGroupStopCommand())
                .then(getBattleGroupBattleCommand());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleGroupStartCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("start")
                .then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("group", StringArgumentType.greedyString())
                        .suggests((context, builder) -> {
                            CobblemonTrainerBattle.groupFiles.keySet().stream().map(String::valueOf).forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            try {
                                BattleGroup.startSession(context);
                                return Command.SINGLE_SUCCESS;

                            } catch (ExecuteCommandFaildException e) {
                                return -1;
                            }
                        }));
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleGroupStopCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("stop")
                .executes(context -> {
                    try {
                        BattleGroup.stopSession(context);
                        return Command.SINGLE_SUCCESS;

                    } catch (ExecuteCommandFaildException e) {
                        return -1;
                    }
                });
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleGroupBattleCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("battle")
                .executes(context -> {
                    try {
                        BattleGroup.battleGroupWithStatusQuo(context);
                        return Command.SINGLE_SUCCESS;

                    } catch (ExecuteCommandFaildException e) {
                        return -1;
                    }
                });
    }
}
