package kiwiapollo.cobblemontrainerbattle.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.RadicalRedTrainerFileScanner;
import kiwiapollo.cobblemontrainerbattle.common.RandomTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;
import net.minecraft.server.command.ServerCommandSource;

public class TrainerBattleFlatCommand extends LiteralArgumentBuilder<ServerCommandSource> {
    public TrainerBattleFlatCommand() {
        super("trainerbattleflat");

        this.requires(new PlayerCommandPredicate(
                String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "trainer"),
                        String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "random")))
                .then(RequiredArgumentBuilder.<ServerCommandSource, String>argument("trainer", StringArgumentType.string())
                        .requires(new PlayerCommandPredicate(String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "trainer")))
                        .suggests((context, builder) -> {
                            RadicalRedTrainerFileScanner.getTrainerFiles().stream()
                                    .map(RadicalRedTrainerFileScanner::toTrainerName)
                                    .forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            TrainerBattle.battleWithFlatLevelAndFullHealth(context, StringArgumentType.getString(context, "trainer"));
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("random")
                        .requires(new PlayerCommandPredicate(String.format("%s.%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral(), "random")))
                        .executes(context -> {
                            TrainerBattle.battleWithFlatLevelAndFullHealth(context, new RandomTrainerFactory().create().name);
                            return Command.SINGLE_SUCCESS;
                        }));
    }
}
