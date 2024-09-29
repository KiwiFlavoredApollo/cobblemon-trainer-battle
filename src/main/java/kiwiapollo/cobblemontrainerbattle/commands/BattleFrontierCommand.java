package kiwiapollo.cobblemontrainerbattle.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.battlefrontier.BattleFrontier;
import net.minecraft.server.command.ServerCommandSource;

public class BattleFrontierCommand extends LiteralArgumentBuilder<ServerCommandSource> {

    public BattleFrontierCommand() {
        super("battlefrontier");

        this.requires(new PlayerCommandPredicate(getLiteral()))
                .then(getBattleFrontierStartCommand())
                .then(getBattleFrontierStopCommand())
                .then(getBattleFrontierBattleCommand())
                .then(getBattleFrontierRerollPokemonsCommand())
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

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierRerollPokemonsCommand() {
        return LiteralArgumentBuilder
                .<ServerCommandSource>literal("reroll")
                .executes(context -> {
                    BattleFrontier.rerollPokemons(context);
                    return Command.SINGLE_SUCCESS;
                });
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierTradePokemonCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("trade")
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
        return LiteralArgumentBuilder.<ServerCommandSource>literal("pokemons")
                .executes(context -> {
                    BattleFrontier.showPartyPokemons(context);
                    return Command.SINGLE_SUCCESS;
                });
    }
}
