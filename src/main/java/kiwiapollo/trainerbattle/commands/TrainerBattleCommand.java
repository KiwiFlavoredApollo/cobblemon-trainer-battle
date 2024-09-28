package kiwiapollo.trainerbattle.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.trainerbattle.battlebuilders.FlatLevelFullHealthTrainerBattleBuilder;
import kiwiapollo.trainerbattle.battlebuilders.TrainerBattleBuilder;
import net.minecraft.server.command.ServerCommandSource;

public class TrainerBattleCommand extends LiteralArgumentBuilder<ServerCommandSource> {
    public TrainerBattleCommand() {
        super("trainerbattle");

        this.requires(new PlayerCommandPredicate(getLiteral()))
                .then(getTrainerBattleCommand())
                .then(getFairTrainerBattleCommand());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getTrainerBattleCommand() {
        return RequiredArgumentBuilder.<ServerCommandSource, String>argument("trainer", StringArgumentType.string())
                .executes(context -> {
                    String trainer = StringArgumentType.getString(context, "trainer");
                    new TrainerBattleBuilder().build(context.getSource().getPlayer());

                    return Command.SINGLE_SUCCESS;
                });
    }

    private ArgumentBuilder<ServerCommandSource, ?> getFairTrainerBattleCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("fair")
                .then(RequiredArgumentBuilder
                        .<ServerCommandSource, String>argument("trainer", StringArgumentType.string())
                        .executes(context -> {
                            String trainer = StringArgumentType.getString(context, "trainer");
                            new FlatLevelFullHealthTrainerBattleBuilder().build(context.getSource().getPlayer());

                            return Command.SINGLE_SUCCESS;
                        }));
    }
}
