package kiwiapollo.cobblemontrainerbattle.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battlefactory.BattleFactory;
import kiwiapollo.cobblemontrainerbattle.battlefactory.BattleFactorySession;
import kiwiapollo.cobblemontrainerbattle.temp.*;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.*;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class BattleFactoryCommand extends LiteralArgumentBuilder<ServerCommandSource> {

    public BattleFactoryCommand() {
        super("battlefactory");

        this.requires(new PlayerCommandPredicate(String.format("%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral())))
                .then(getBattleFactoryStartSessionCommand())
                .then(getBattleFactoryStopSessionCommand())
                .then(getBattleFactoryStartBattleCommand())
                .then(getBattleFactoryRerollPokemonsCommand())
                .then(getBattleFactoryTradePokemonsCommand())
                .then(getBattleFactoryShowPokemonsCommand());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFactoryStartSessionCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("startsession")
                .executes(BattleFactory::startSession);
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFactoryStopSessionCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("stopsession")
                .executes(BattleFactory::stopSession);
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFactoryStartBattleCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("startbattle")
                .executes(BattleFactory::startBattle);
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFactoryRerollPokemonsCommand() {
        return LiteralArgumentBuilder
                .<ServerCommandSource>literal("rerollpokemon")
                .executes(BattleFactory::rerollPokemon);
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFactoryTradePokemonsCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("tradepokemon")
                .executes(BattleFactory::showTradeablePokemon)
                .then(RequiredArgumentBuilder
                        .<ServerCommandSource, Integer>argument("playerslot", IntegerArgumentType.integer(1, 3))
                        .then(RequiredArgumentBuilder
                                .<ServerCommandSource, Integer>argument("trainerslot", IntegerArgumentType.integer(1, 3))
                                .executes(BattleFactory::tradePokemons)));
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFactoryShowPokemonsCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("showpokemon")
                .executes(BattleFactory::showPartyPokemon);
    }
}
