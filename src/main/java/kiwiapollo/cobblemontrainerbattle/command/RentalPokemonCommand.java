package kiwiapollo.cobblemontrainerbattle.command;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.PokemonStatusPrinter;
import kiwiapollo.cobblemontrainerbattle.parser.player.BattleContextStorage;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Objects;

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
                .executes(context -> {
                    try {
                        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();

                        PartyStore rentalPokemon = new PartyStore(player.getUuid());
                        rentalPokemon.add(PokemonSpecies.INSTANCE.random().create(50));
                        rentalPokemon.add(PokemonSpecies.INSTANCE.random().create(50));
                        rentalPokemon.add(PokemonSpecies.INSTANCE.random().create(50));

                        BattleContextStorage.getInstance().getOrCreate(player.getUuid()).setRentalPokemon(rentalPokemon);

                        return Command.SINGLE_SUCCESS;

                    } catch (CommandSyntaxException e) {
                        return 0;
                    }
                });
    }

    private ArgumentBuilder<ServerCommandSource, ?> getCloneRentalPokemonCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("clone")
                .executes(context -> {
                    try {
                        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();

                        PartyStore original = Cobblemon.INSTANCE.getStorage().getParty(player);
                        PartyStore clone = new PartyStore(player.getUuid());

                        if (original.occupied() < 3) {
                            return 0;
                        }

                        for (Pokemon pokemon : original.toGappyList().stream().filter(Objects::nonNull).toList()) {
                            clone.add(pokemon.clone(true, true));
                        }

                        PartyStore truncated = new PartyStore(player.getUuid());
                        clone.toGappyList().stream()
                                .filter(Objects::nonNull)
                                .toList().subList(0, 3)
                                .forEach(truncated::add);

                        BattleContextStorage.getInstance().getOrCreate(player.getUuid()).setRentalPokemon(truncated);

                        return Command.SINGLE_SUCCESS;

                    } catch (CommandSyntaxException e) {
                        return 0;
                    }
                });
    }

    private ArgumentBuilder<ServerCommandSource, ?> getTradeRentalPokemonCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("trade")
                .then(RequiredArgumentBuilder
                        .<ServerCommandSource, Integer>argument("playerslot", IntegerArgumentType.integer(1, 3))
                        .then(RequiredArgumentBuilder
                                .<ServerCommandSource, Integer>argument("trainerslot", IntegerArgumentType.integer(1, 3))
                                .executes(context -> {
//                                    Pokemon trainerPokemon = trainer.getParty().get(trainerSlot - 1);
//                                    Pokemon playerPokemon = player.getParty().get(playerSlot - 1);
//
//                                    trainer.getParty().set(trainerSlot, playerPokemon);
//                                    player.getParty().set(playerSlot, trainerPokemon);
//
//                                    isPokemonTraded = true;
//
//                                    player.sendInfoMessage(Text.translatable("command.cobblemontrainerbattle.success.battlefactory.tradepokemon", playerPokemon.getDisplayName(), trainerPokemon.getDisplayName()));
//                                    CobblemonTrainerBattle.LOGGER.info("{} traded {} for {}", player.getName(), playerPokemon.getDisplayName(), trainerPokemon.getDisplayName());

                                    return Command.SINGLE_SUCCESS;
                                })));
    }

    private ArgumentBuilder<ServerCommandSource, ?> getShowRentalPokemonCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("show")
                .executes(context -> {
                    ServerPlayerEntity player = context.getSource().getPlayerOrThrow();

                    new PokemonStatusPrinter(player).print(BattleContextStorage.getInstance().getOrCreate(player.getUuid()).getRentalPokemon());

                    return Command.SINGLE_SUCCESS;
                });
    }

    private ArgumentBuilder<ServerCommandSource, ?> getClearRentalPokemonCommand() {
        return LiteralArgumentBuilder.<ServerCommandSource>literal("clear")
                .executes(context -> {
                    try {
                        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();

                        BattleContextStorage.getInstance().getOrCreate(player.getUuid()).clearRentalPokemon();

                        return Command.SINGLE_SUCCESS;

                    } catch (CommandSyntaxException e) {
                        return 0;
                    }
                });
    }
}
