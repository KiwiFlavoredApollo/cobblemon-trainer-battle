package kiwiapollo.cobblemontrainerbattle.command.executor;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.preset.RentalBattlePreset;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContext;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContextStorage;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class RentalPokemonTrader implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();

            if (canTradePokemon(player)) {
                player.sendMessage(Text.translatable("command.cobblemontrainerbattle.error.rentalpokemon_trade"));
                return 0;
            }

            int playerslot = IntegerArgumentType.getInteger(context, "playerslot");
            int trainerslot = IntegerArgumentType.getInteger(context, "trainerslot");

            Pokemon playerPokemon = getPlayerPokemon(player, playerslot);
            Pokemon trainerPokemon = getTrainerPokemon(player, trainerslot);

            setRentalPokemon(player, playerslot, trainerPokemon);

            player.sendMessage(Text.translatable("command.cobblemontrainerbattle.success.battlefactory.tradepokemon", playerPokemon.getDisplayName(), trainerPokemon.getDisplayName()));
            CobblemonTrainerBattle.LOGGER.info("{} traded {} for {}", player.getName(), playerPokemon.getDisplayName(), trainerPokemon.getDisplayName());

            return Command.SINGLE_SUCCESS;

        } catch (CommandSyntaxException e) {
            return 0;
        }
    }

    private boolean canTradePokemon(ServerPlayerEntity player) {
        BattleContext context = BattleContextStorage.getInstance().getOrCreate(player.getUuid());
        return context.getTradablePokemon().occupied() < RentalBattlePreset.PARTY_SIZE;
    }

    private void setRentalPokemon(ServerPlayerEntity player, int slot, Pokemon trainerPokemon) {
        BattleContext context = BattleContextStorage.getInstance().getOrCreate(player.getUuid());
        context.getRentalPokemon().set(slot, trainerPokemon);
        context.clearTradablePokemon();
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
