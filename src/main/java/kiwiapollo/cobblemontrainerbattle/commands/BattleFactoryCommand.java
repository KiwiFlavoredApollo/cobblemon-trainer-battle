package kiwiapollo.cobblemontrainerbattle.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battlefactory.BattleFactory;
import net.minecraft.server.command.ServerCommandSource;

public class BattleFactoryCommand extends LiteralArgumentBuilder<ServerCommandSource> {

    public BattleFactoryCommand() {
        super("battlefactory");

        this.requires(new PlayerCommandPredicate(String.format("%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral())))
                .then(getBattleFactoryQuickStartCommand())
                .then(getBattleFactoryStartSessionCommand())
                .then(getBattleFactoryStopSessionCommand())
                .then(getBattleFactoryStartBattleCommand())
                .then(getBattleFactoryRerollPokemonsCommand())
                .then(getBattleFactoryTradePokemonsCommand())
                .then(getBattleFactoryShowPokemonsCommand())
                .then(getBattleFactoryShowWinningStreakCommand());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFactoryQuickStartCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("quickstart")
                .executes(BattleFactory::quickStart);
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
                .<ServerCommandSource>literal("rerollpokemons")
                .executes(BattleFactory::rerollPokemons);
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFactoryTradePokemonsCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("tradepokemons")
                .executes(BattleFactory::showTradeablePokemons)
                .then(RequiredArgumentBuilder
                        .<ServerCommandSource, Integer>argument("playerslot", IntegerArgumentType.integer(1, 3))
                        .then(RequiredArgumentBuilder
                                .<ServerCommandSource, Integer>argument("trainerslot", IntegerArgumentType.integer(1, 3))
                                .executes(BattleFactory::tradePokemons)));
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFactoryShowPokemonsCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("showpokemons")
                .executes(BattleFactory::showPartyPokemons);
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFactoryShowWinningStreakCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("winningstreak")
                .executes(BattleFactory::showWinningStreak);
    }
}
