package kiwiapollo.cobblemontrainerbattle.command.rentalpokemon;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.battle.TradablePokemon;
import kiwiapollo.cobblemontrainerbattle.battle.TradablePokemonStorage;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TradablePokemonStatusPrinter extends PokemonStatusPrinter implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        TradablePokemon pokemon = TradablePokemonStorage.getInstance().get(player);

        if (pokemon.occupied() == 0) {
            player.sendMessage(Text.translatable("commands.cobblemontrainerbattle.rentalpokemon.showtradable.failed.tradable_pokemon_not_exist").formatted(Formatting.RED));
            return 0;
        }

        print(pokemon, player);

        return Command.SINGLE_SUCCESS;
    }
}
