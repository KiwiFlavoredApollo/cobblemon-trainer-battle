package kiwiapollo.cobblemontrainerbattle.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battlefrontier.BattleFrontier;
import net.minecraft.server.command.ServerCommandSource;

public class BattleFrontierCommand extends LiteralArgumentBuilder<ServerCommandSource> {

    public BattleFrontierCommand() {
        super("battlefrontier");

        this.requires(new PlayerCommandPredicate(String.format("%s.%s", CobblemonTrainerBattle.NAMESPACE, getLiteral())))
                .then(getBattleFrontierQuickStartCommand())
                .then(getBattleFrontierStartCommand())
                .then(getBattleFrontierStopCommand())
                .then(getBattleFrontierBattleCommand())
                .then(getBattleFrontierRerollPokemonsCommand())
                .then(getBattleFrontierTradePokemonCommand())
                .then(getBattleFrontierShowPokemonsCommand())
                .then(getBattleFrontierShowWinningStreakCommand());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierQuickStartCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("quickstart")
                .executes(BattleFrontier::quickStart);
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierStartCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("startsession")
                .executes(BattleFrontier::startSession);
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierStopCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("stopsession")
                .executes(BattleFrontier::stopSession);
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierBattleCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("startbattle")
                .executes(BattleFrontier::startBattle);
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierRerollPokemonsCommand() {
        return LiteralArgumentBuilder
                .<ServerCommandSource>literal("rerollpokemons")
                .executes(BattleFrontier::rerollPokemons);
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierTradePokemonCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("tradepokemons")
                .executes(BattleFrontier::showTradeablePokemons)
                .then(RequiredArgumentBuilder
                        .<ServerCommandSource, Integer>argument("playerslot", IntegerArgumentType.integer(1, 3))
                        .then(RequiredArgumentBuilder
                                .<ServerCommandSource, Integer>argument("trainerslot", IntegerArgumentType.integer(1, 3))
                                .executes(BattleFrontier::tradePokemons)));
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierShowPokemonsCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("showpokemons")
                .executes(BattleFrontier::showPartyPokemons);
    }

    private ArgumentBuilder<ServerCommandSource, ?> getBattleFrontierShowWinningStreakCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("winningstreak")
                .executes(BattleFrontier::showWinningStreak);
    }
}
