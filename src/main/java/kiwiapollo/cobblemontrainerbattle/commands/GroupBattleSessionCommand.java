package kiwiapollo.cobblemontrainerbattle.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.groupbattle.GroupBattle;
import net.minecraft.server.command.ServerCommandSource;

public class GroupBattleSessionCommand extends LiteralArgumentBuilder<ServerCommandSource> {

    public GroupBattleSessionCommand() {
        super("groupbattlesession");

        this.requires(new PlayerCommandPredicate(String.format("%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral())))
                .executes(GroupBattle::stopSession);
    }
}
