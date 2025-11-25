package kiwiapollo.cobblemontrainerbattle.command.rentalpokemon;

import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.context.TradePokemonStorage;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TradePokemonStatusPrinter extends PokemonStatusPrinter implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        PartyStore pokemon = getTradePokemon(player);

        if (pokemon.occupied() == 0) {
            player.sendMessage(Text.translatable("command.cobblemontrainerbattle.error.rentalpokemon.tradable_pokemon_not_exist").formatted(Formatting.RED));
            return 0;
        }

        printPokemonStatus(pokemon, player);

        return Command.SINGLE_SUCCESS;
    }

    private PartyStore getTradePokemon(ServerPlayerEntity player) {
        PartyStore pokemon = new PartyStore(player.getUuid());

        pokemon.add(TradePokemonStorage.getInstance().get(player).getFirst());
        pokemon.add(TradePokemonStorage.getInstance().get(player).getSecond());
        pokemon.add(TradePokemonStorage.getInstance().get(player).getThird());

        return pokemon;
    }
}
