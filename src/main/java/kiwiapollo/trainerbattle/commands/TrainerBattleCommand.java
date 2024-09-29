package kiwiapollo.trainerbattle.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.trainerbattle.TrainerBattle;
import kiwiapollo.trainerbattle.battlebuilders.FlatLevelFullHealthTrainerBattleBuilder;
import kiwiapollo.trainerbattle.battlebuilders.TrainerBattleBuilder;
import kiwiapollo.trainerbattle.common.NameTrainerFactory;
import kiwiapollo.trainerbattle.exceptions.TrainerNameNotExistException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

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
                    try {
                        String trainer = StringArgumentType.getString(context, "trainer");
                        new TrainerBattleBuilder().build(
                                context.getSource().getPlayer(), new NameTrainerFactory().create(trainer));
                        return Command.SINGLE_SUCCESS;

                    } catch (TrainerNameNotExistException e) {
                        String trainer = StringArgumentType.getString(context, "trainer");
                        context.getSource().getPlayer().sendMessage(
                                Text.literal(String.format("Trainer %s does not exist", trainer)));
                        TrainerBattle.LOGGER.error(String.format("Trainer %s does not exist", trainer));
                        return -1;
                    }
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
