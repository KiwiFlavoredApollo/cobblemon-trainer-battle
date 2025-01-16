package kiwiapollo.cobblemontrainerbattle.command.executor;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.parser.player.BattleContextStorage;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class RentalPokemonRemover implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();

            BattleContextStorage.getInstance().getOrCreate(player.getUuid()).clearRentalPokemon();

            return Command.SINGLE_SUCCESS;

        } catch (CommandSyntaxException e) {
            return 0;
        }
    }
}
