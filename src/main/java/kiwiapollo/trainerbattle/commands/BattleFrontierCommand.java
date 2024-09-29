package kiwiapollo.trainerbattle.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.trainerbattle.battlefrontier.BattleFrontier;
import net.minecraft.server.command.ServerCommandSource;

public class BattleFrontierCommand extends LiteralArgumentBuilder<ServerCommandSource> {

    public BattleFrontierCommand() {
        super("battlefrontier");

        this.requires(new PlayerCommandPredicate(getLiteral()))
                .then(getBattleFrontierStartCommand())
                .then(getBattleFrontierStopCommand())
                .then(getBattleFrontierBattleCommand())
                .then(getBattleFrontierStartingPokemonsCommand())
                .then(getBattleFrontierTradePokemonCommand())
                .then(getBattleFrontierShowPokemonsCommand());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierStartCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("start")
                .executes(context -> {
                    BattleFrontier.start(context);
                    return Command.SINGLE_SUCCESS;
                });
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierStopCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("stop")
                .executes(context -> {
                    BattleFrontier.stop(context);
                    return Command.SINGLE_SUCCESS;
                });
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierBattleCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("battle")
                .executes(context -> {
                    BattleFrontier.battle(context);
                    return Command.SINGLE_SUCCESS;
                });
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierStartingPokemonsCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("startingpokemons")
                .then(getBattleFrontierRerollStartingPokemonCommand())
                .then(getBattleFrontierConfirmStartingPokemonCommand())
                .executes(context -> {
                    BattleFrontier.showStartingPokemons(context);
                    return Command.SINGLE_SUCCESS;
                });
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierRerollStartingPokemonCommand() {
        return LiteralArgumentBuilder
                .<ServerCommandSource>literal("reroll")
                .executes(context -> {
                    BattleFrontier.rerollStartingPokemons(context);
                    return Command.SINGLE_SUCCESS;
                });
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierConfirmStartingPokemonCommand() {
        return LiteralArgumentBuilder
                .<ServerCommandSource>literal("confirm")
                .executes(context -> {
                    BattleFrontier.confirmStartingPokemons(context);
                    return Command.SINGLE_SUCCESS;
                });
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierTradePokemonCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("tradepokemon")
                .then(RequiredArgumentBuilder
                        .<ServerCommandSource, Integer>argument("playerslot", IntegerArgumentType.integer(1, 3))
                        .then(RequiredArgumentBuilder
                                .<ServerCommandSource, Integer>argument("trainerslot", IntegerArgumentType.integer(1, 3))
                                .executes(context -> {
                                    BattleFrontier.tradePokemon(context);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .executes(context -> {
                    BattleFrontier.showTradeablePokemons(context);
                    return Command.SINGLE_SUCCESS;
                });
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierShowPokemonsCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("showpokemons")
                .executes(context -> {
                    BattleFrontier.showPartyPokemons(context);
                    return Command.SINGLE_SUCCESS;
                });
    }
}
