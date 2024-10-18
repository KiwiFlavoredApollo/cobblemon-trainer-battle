package kiwiapollo.cobblemontrainerbattle.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.groupbattle.GroupBattle;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

public class GroupBattleCommand extends LiteralArgumentBuilder<ServerCommandSource> {

    public GroupBattleCommand() {
        super("groupbattle");

        this.requires(new PlayerCommandPredicate(String.format("%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral())))
                .then(getStartSessionCommand())
                .then(getStopSessionCommand())
                .then(getStartBattleCommand());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getStartSessionCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("startsession")
                .then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("group", StringArgumentType.greedyString())
                        .suggests((context, builder) -> {
                            CobblemonTrainerBattle.trainerGroupProfileRegistry.keySet().stream()
                                    .map(Identifier::getPath)
                                    .forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .executes(GroupBattle::startNormalGroupBattleSession));
    }

    private ArgumentBuilder<ServerCommandSource, ?> getStopSessionCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("stopsession")
                .executes(GroupBattle::stopSession);
    }

    private ArgumentBuilder<ServerCommandSource, ?> getStartBattleCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("startbattle")
                .executes(GroupBattle::startBattle);
    }
}
