package kiwiapollo.cobblemontrainerbattle.command.rentalpokemon;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.battle.RentalPokemonStorage;
import kiwiapollo.cobblemontrainerbattle.battle.TradablePokemonStorage;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class RentalPokemonRemover implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();

            RentalPokemonStorage.getInstance().get(player).clear();
            TradablePokemonStorage.getInstance().get(player).clear();

            return Command.SINGLE_SUCCESS;

        } catch (CommandSyntaxException e) {
            return 0;
        }
    }
}
