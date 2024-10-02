package kiwiapollo.cobblemontrainerbattle.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battlefrontier.BattleFrontier;
import kiwiapollo.cobblemontrainerbattle.exceptions.ExecuteCommandFailedException;
import net.minecraft.server.command.ServerCommandSource;

public class BattleFrontierCommand extends LiteralArgumentBuilder<ServerCommandSource> {

    public BattleFrontierCommand() {
        super("battlefrontier");

        this.requires(new PlayerCommandPredicate(String.format("%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral())))
                .then(getBattleFrontierStartCommand())
                .then(getBattleFrontierStopCommand())
                .then(getBattleFrontierBattleCommand())
                .then(getBattleFrontierRerollPokemonsCommand())
                .then(getBattleFrontierTradePokemonCommand())
                .then(getBattleFrontierShowPokemonsCommand())
                .then(getBattleFrontierShowWinningStreakCommand());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierStartCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("start")
                .executes(context -> {
                    try {
                        BattleFrontier.startSession(context);
                        return Command.SINGLE_SUCCESS;

                    } catch (ExecuteCommandFailedException e) {
                        return -1;
                    }
                });
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierStopCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("stop")
                .executes(context -> {
                    try {
                        BattleFrontier.stopSession(context);
                        return Command.SINGLE_SUCCESS;

                    } catch (ExecuteCommandFailedException e) {
                        return -1;
                    }
                });
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierBattleCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("battle")
                .executes(context -> {
                    try {
                        BattleFrontier.startBattle(context);
                        return Command.SINGLE_SUCCESS;

                    } catch (ExecuteCommandFailedException e) {
                        return -1;
                    }
                });
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierRerollPokemonsCommand() {
        return LiteralArgumentBuilder
                .<ServerCommandSource>literal("reroll")
                .executes(context -> {
                    try {
                        BattleFrontier.rerollPokemons(context);
                        return Command.SINGLE_SUCCESS;

                    } catch (ExecuteCommandFailedException e) {
                        return -1;
                    }
                });
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierTradePokemonCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("trade")
                .then(RequiredArgumentBuilder
                        .<ServerCommandSource, Integer>argument("playerslot", IntegerArgumentType.integer(1, 3))
                        .then(RequiredArgumentBuilder
                                .<ServerCommandSource, Integer>argument("trainerslot", IntegerArgumentType.integer(1, 3))
                                .executes(context -> {
                                    try {
                                        BattleFrontier.tradePokemon(context);
                                        return Command.SINGLE_SUCCESS;

                                    } catch (ExecuteCommandFailedException e) {
                                        return -1;
                                    }
                                })))
                .executes(context -> {
                    try {
                        BattleFrontier.showTradeablePokemons(context);
                        return Command.SINGLE_SUCCESS;

                    } catch (ExecuteCommandFailedException e) {
                        return -1;
                    }
                });
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierShowPokemonsCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("pokemons")
                .executes(context -> {
                    try {
                        BattleFrontier.showPartyPokemons(context);
                        return Command.SINGLE_SUCCESS;

                    } catch (ExecuteCommandFailedException e) {
                        return -1;
                    }
                });
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierShowWinningStreakCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("streak")
                .executes(context -> {
                    try {
                        BattleFrontier.showWinningStreak(context);
                        return Command.SINGLE_SUCCESS;

                    } catch (ExecuteCommandFailedException e) {
                        return -1;
                    }
                });
    }
}
