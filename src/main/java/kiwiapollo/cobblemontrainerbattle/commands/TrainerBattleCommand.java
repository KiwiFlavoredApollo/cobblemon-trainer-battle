package kiwiapollo.cobblemontrainerbattle.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.common.RadicalRedTrainerFileScanner;
import kiwiapollo.cobblemontrainerbattle.common.RandomTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;
import net.minecraft.server.command.ServerCommandSource;

public class TrainerBattleCommand extends LiteralArgumentBuilder<ServerCommandSource> {
    public TrainerBattleCommand() {
        super("trainerbattle");

        this.requires(new PlayerCommandPredicate(getLiteral()))
                .then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("trainer", StringArgumentType.string())
                        .suggests((context, builder) -> {
                            RadicalRedTrainerFileScanner.getTrainerFiles().stream()
                                    .map(RadicalRedTrainerFileScanner::toTrainerName)
                                    .forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            TrainerBattle.battleWithStatusQuo(context, StringArgumentType.getString(context, "trainer"));
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("random")
                        .executes(context -> {
                            TrainerBattle.battleWithStatusQuo(context, new RandomTrainerFactory().create().name);
                            return Command.SINGLE_SUCCESS;
                        }));
    }
}
