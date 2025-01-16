package kiwiapollo.cobblemontrainerbattle.command.executor;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContext;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContextStorage;
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
                player.sendMessage(Text.translatable("command.cobblemontrainerbattle.error.rentalpokemon.rental_pokemon_not_exist").formatted(Formatting.RED));
                return 0;
            }

            if (!isTradablePokemonExist(player)) {
                player.sendMessage(Text.translatable("command.cobblemontrainerbattle.error.rentalpokemon.tradable_pokemon_not_exist").formatted(Formatting.RED));
                return 0;
            }

            int playerslot = IntegerArgumentType.getInteger(context, "playerslot");
            int trainerslot = IntegerArgumentType.getInteger(context, "trainerslot");

            Pokemon playerPokemon = getPlayerPokemon(player, playerslot);
            Pokemon trainerPokemon = getTrainerPokemon(player, trainerslot);

            setRentalPokemon(player, playerslot, trainerPokemon);

            clearTradablePokemon(player);

            player.sendMessage(Text.translatable("command.cobblemontrainerbattle.success.rentalpokemon.trade", playerPokemon.getDisplayName(), trainerPokemon.getDisplayName()));
            CobblemonTrainerBattle.LOGGER.info("{} traded {} for {}", player.getName(), playerPokemon.getDisplayName(), trainerPokemon.getDisplayName());

            return Command.SINGLE_SUCCESS;

        } catch (CommandSyntaxException e) {
            return 0;
        }
    }

    private boolean isRentalPokemonExist(ServerPlayerEntity player) {
        BattleContext context = BattleContextStorage.getInstance().getOrCreate(player.getUuid());
        return context.getRentalPokemon().occupied() != 0;
    }

    private void clearTradablePokemon(ServerPlayerEntity player) {
        BattleContext context = BattleContextStorage.getInstance().getOrCreate(player.getUuid());
        context.clearTradablePokemon();
    }

    private boolean isTradablePokemonExist(ServerPlayerEntity player) {
        BattleContext context = BattleContextStorage.getInstance().getOrCreate(player.getUuid());
        return context.getTradablePokemon().occupied() != 0;
    }

    private void setRentalPokemon(ServerPlayerEntity player, int slot, Pokemon trainerPokemon) {
        BattleContext context = BattleContextStorage.getInstance().getOrCreate(player.getUuid());
        context.getRentalPokemon().set(slot, trainerPokemon);
    }

    private Pokemon getPlayerPokemon(ServerPlayerEntity player, int slot) {
        BattleContext context = BattleContextStorage.getInstance().getOrCreate(player.getUuid());
        return context.getRentalPokemon().get(slot);
    }

    private Pokemon getTrainerPokemon(ServerPlayerEntity player, int slot) {
        BattleContext context = BattleContextStorage.getInstance().getOrCreate(player.getUuid());
        return context.getTradablePokemon().get(slot);
    }
}
