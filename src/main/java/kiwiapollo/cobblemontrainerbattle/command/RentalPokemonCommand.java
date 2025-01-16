package kiwiapollo.cobblemontrainerbattle.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.*;
import kiwiapollo.cobblemontrainerbattle.command.predicate.PlayerCommandSourcePredicate;
import kiwiapollo.cobblemontrainerbattle.common.RentalBattle;
import net.minecraft.server.command.ServerCommandSource;

public class RentalPokemonCommand extends LiteralArgumentBuilder<ServerCommandSource> {
    public RentalPokemonCommand() {
        super("rentalpokemon");

        this.requires(new PlayerCommandSourcePredicate(String.format("%s.%s", CobblemonTrainerBattle.MOD_ID, getLiteral())))
                .then(getRandomRentalPokemonCommand())
                .then(getCloneRentalPokemonCommand())
                .then(getTradeRentalPokemonCommand())
                .then(getShowRentalPokemonCommand())
                .then(getClearRentalPokemonCommand());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getRandomRentalPokemonCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("random")
                .executes(new RandomRentalPokemonProvider());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getCloneRentalPokemonCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("clone")
                .executes(new CloneRentalPokemonProvider());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getTradeRentalPokemonCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("trade")
                .then(RequiredArgumentBuilder
                        .<ServerCommandSource, Integer>argument("playerslot", IntegerArgumentType.integer(1, RentalBattle.PARTY_SIZE))
                        .then(RequiredArgumentBuilder
                                .<ServerCommandSource, Integer>argument("trainerslot", IntegerArgumentType.integer(1, RentalBattle.PARTY_SIZE))
                                .executes(new RentalPokemonTrader())))
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("show")
                        .executes(new TradablePokemonStatusPrinter()));
    }

    private ArgumentBuilder<ServerCommandSource, ?> getShowRentalPokemonCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("show")
                .executes(new RentalPokemonStatusPrinter());
    }

    private ArgumentBuilder<ServerCommandSource, ?> getClearRentalPokemonCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("clear")
                .executes(new RentalPokemonRemover());
    }
}
