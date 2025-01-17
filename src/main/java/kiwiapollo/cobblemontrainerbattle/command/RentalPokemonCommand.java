package kiwiapollo.cobblemontrainerbattle.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.command.executor.*;
import kiwiapollo.cobblemontrainerbattle.command.predicate.PlayerCommandSourcePredicate;
import kiwiapollo.cobblemontrainerbattle.battle.preset.RentalBattlePreset;
import net.minecraft.server.command.ServerCommandSource;

public class RentalPokemonCommand extends LiteralArgumentBuilder<ServerCommandSource> {
    public RentalPokemonCommand() {
        super("rentalpokemon");

        this.requires(new PlayerCommandSourcePredicate(String.format("%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral())))
                .then(getRandomRentalPokemonCommand())
                .then(getCloneRentalPokemonCommand())
                .then(getTradeRentalPokemonCommand())
                .then(getShowRentalPokemonCommand())
                .then(getShowTradablePokemonCommand())
                .then(getClearRentalPokemonCommand());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getRandomRentalPokemonCommand() {
        String subcommand = "random";
        return LiteralArgumentBuilder.<ServerCommandSource>literal(subcommand)
                .requires(new PlayerCommandSourcePredicate(String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), subcommand)))
                .executes(new RandomRentalPokemonProvider());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getCloneRentalPokemonCommand() {
        String subcommand = "clone";
        return LiteralArgumentBuilder.<ServerCommandSource>literal(subcommand)
                .requires(new PlayerCommandSourcePredicate(String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), subcommand)))
                .executes(new CloneRentalPokemonProvider());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getTradeRentalPokemonCommand() {
        String subcommand = "trade";
        return LiteralArgumentBuilder.<ServerCommandSource>literal(subcommand)
                .requires(new PlayerCommandSourcePredicate(String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), subcommand)))
                .then(RequiredArgumentBuilder
                        .<ServerCommandSource, Integer>argument("playerslot", IntegerArgumentType.integer(1, RentalBattlePreset.PARTY_SIZE))
                        .then(RequiredArgumentBuilder
                                .<ServerCommandSource, Integer>argument("trainerslot", IntegerArgumentType.integer(1, RentalBattlePreset.PARTY_SIZE))
                                .executes(new RentalPokemonTrader())));
    }

    private ArgumentBuilder<ServerCommandSource, ?> getShowRentalPokemonCommand() {
        String subcommand = "showrental";
        return LiteralArgumentBuilder.<ServerCommandSource>literal(subcommand)
                .requires(new PlayerCommandSourcePredicate(String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), subcommand)))
                .executes(new RentalPokemonStatusPrinter());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getShowTradablePokemonCommand() {
        String subcommand = "showtrade";
        return LiteralArgumentBuilder.<ServerCommandSource>literal(subcommand)
                .requires(new PlayerCommandSourcePredicate(String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), subcommand)))
                .executes(new TradablePokemonStatusPrinter());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getClearRentalPokemonCommand() {
        String subcommand = "clear";
        return LiteralArgumentBuilder.<ServerCommandSource>literal(subcommand)
                .requires(new PlayerCommandSourcePredicate(String.format("%s.%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral(), subcommand)))
                .executes(new RentalPokemonRemover());
    }
}
