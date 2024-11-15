package kiwiapollo.cobblemontrainerbattle.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.groupbattle.NormalGroupBattle;
import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerGroupProfileStorage;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

public class GroupBattleCommand extends LiteralArgumentBuilder<ServerCommandSource> {

    public GroupBattleCommand() {
        super("groupbattle");

        this.requires(new PlayerCommandSourcePredicate(String.format("%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral())))
                .then(getStartSessionCommand())
                .then(getStopSessionCommand())
                .then(getStartBattleCommand());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getStartSessionCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("startsession")
                .then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("group", StringArgumentType.greedyString())
                        .suggests((context, builder) -> {
                            TrainerGroupProfileStorage.getProfileRegistry().keySet().stream()
                                    .map(Identifier::toString)
                                    .forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .executes(NormalGroupBattle::startSession));
    }

    private ArgumentBuilder<ServerCommandSource, ?> getStopSessionCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("stopsession")
                .executes(NormalGroupBattle::stopSession);
    }

    private ArgumentBuilder<ServerCommandSource, ?> getStartBattleCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("startbattle")
                .executes(NormalGroupBattle::startBattle);
    }
}
