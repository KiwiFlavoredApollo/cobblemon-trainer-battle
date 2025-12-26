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
                player.sendMessage(getNoRentalPokemonErrorMessage());
                return 0;
            }

            if (!isTradablePokemonExist(player)) {
                player.sendMessage(getNoTradablePokemonErrorMessage());
                return 0;
            }

            int playerSlot = IntegerArgumentType.getInteger(context, "playerslot");
            int trainerSlot = IntegerArgumentType.getInteger(context, "trainerslot");

            Pokemon playerPokemon = RentalPokemonStorage.getInstance().get(player).get(toIndex(playerSlot));
            Pokemon trainerPokemon = TradablePokemonStorage.getInstance().get(player).get(toIndex(trainerSlot));

            RentalPokemonStorage.getInstance().get(player).set(toIndex(playerSlot), trainerPokemon);
            TradablePokemonStorage.getInstance().get(player).clear();

            player.sendMessage(getRentalPokemonTradeSuccessMessage(playerPokemon, trainerPokemon));
            CobblemonTrainerBattle.LOGGER.info("{} traded {} for {}", player.getName(), playerPokemon.getDisplayName(), trainerPokemon.getDisplayName());

            return Command.SINGLE_SUCCESS;

        } catch (CommandSyntaxException e) {
            return 0;
        }
    }

    private boolean isRentalPokemonExist(ServerPlayerEntity player) {
        return RentalPokemonStorage.getInstance().get(player).occupied() != 0;
    }

    private Text getNoRentalPokemonErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.rentalpokemon.trade.error.no_rental_pokemon").formatted(Formatting.RED);
    }

    private boolean isTradablePokemonExist(ServerPlayerEntity player) {
        return TradablePokemonStorage.getInstance().get(player).occupied() != 0;
    }

    private Text getNoTradablePokemonErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.rentalpokemon.trade.error.no_tradable_pokemon").formatted(Formatting.RED);
    }

    private int toIndex(int slot) {
        return slot - 1;
    }

    private Text getRentalPokemonTradeSuccessMessage(Pokemon playerPokemon, Pokemon trainerPokemon) {
        return Text.translatable("commands.cobblemontrainerbattle.rentalpokemon.trade.success", playerPokemon.getDisplayName(), trainerPokemon.getDisplayName());
    }
}
