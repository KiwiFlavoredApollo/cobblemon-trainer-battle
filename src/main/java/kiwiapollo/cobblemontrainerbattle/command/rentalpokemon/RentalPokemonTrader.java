package kiwiapollo.cobblemontrainerbattle.command.rentalpokemon;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.RentalPokemonStorage;
import kiwiapollo.cobblemontrainerbattle.battle.TradablePokemonStorage;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class RentalPokemonTrader implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();

            if (!isRentalPokemonExist(player)) {
                player.sendMessage(Text.translatable("commands.cobblemontrainerbattle.rentalpokemon.trade.failed.rental_pokemon_not_exist").formatted(Formatting.RED));
                return 0;
            }

            if (!isTradablePokemonExist(player)) {
                player.sendMessage(Text.translatable("commands.cobblemontrainerbattle.rentalpokemon.trade.failed.tradable_pokemon_not_exist").formatted(Formatting.RED));
                return 0;
            }

            int playerslot = IntegerArgumentType.getInteger(context, "playerslot");
            int trainerslot = IntegerArgumentType.getInteger(context, "trainerslot");

            Pokemon playerPokemon = getPlayerPokemon(player, playerslot);
            Pokemon trainerPokemon = getTrainerPokemon(player, trainerslot);

            setRentalPokemon(player, playerslot, trainerPokemon);

            clearTradablePokemon(player);

            player.sendMessage(Text.translatable("commands.cobblemontrainerbattle.rentalpokemon.trade.success", playerPokemon.getDisplayName(), trainerPokemon.getDisplayName()));
            CobblemonTrainerBattle.LOGGER.info("{} traded {} for {}", player.getName(), playerPokemon.getDisplayName(), trainerPokemon.getDisplayName());

            return Command.SINGLE_SUCCESS;

        } catch (CommandSyntaxException e) {
            return 0;
        }
    }

    private boolean isRentalPokemonExist(ServerPlayerEntity player) {
        return RentalPokemonStorage.getInstance().get(player).occupied() != 0;
    }

    private void clearTradablePokemon(ServerPlayerEntity player) {
        TradablePokemonStorage.getInstance().get(player).clear();
    }

    private boolean isTradablePokemonExist(ServerPlayerEntity player) {
        return TradablePokemonStorage.getInstance().get(player).occupied() != 0;
    }

    private void setRentalPokemon(ServerPlayerEntity player, int slot, Pokemon pokemon) {
        RentalPokemonStorage.getInstance().get(player).set(toIndex(slot), pokemon);
    }

    private Pokemon getPlayerPokemon(ServerPlayerEntity player, int slot) {
        return RentalPokemonStorage.getInstance().get(player).get(toIndex(slot));
    }

    private Pokemon getTrainerPokemon(ServerPlayerEntity player, int slot) {
        return TradablePokemonStorage.getInstance().get(player).get(toIndex(slot));
    }

    private int toIndex(int slot) {
        return slot - 1;
    }
}
