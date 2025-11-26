package kiwiapollo.cobblemontrainerbattle.command.rentalpokemon;

import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.battle.RentalPokemon;
import kiwiapollo.cobblemontrainerbattle.battle.RentalPokemonStorage;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class RentalPokemonStatusPrinter extends PokemonStatusPrinter implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        RentalPokemon pokemon = RentalPokemonStorage.getInstance().get(player);

        if (pokemon.occupied() == 0) {
            player.sendMessage(Text.translatable("command.cobblemontrainerbattle.error.rentalpokemon.rental_pokemon_not_exist").formatted(Formatting.RED));
            return 0;
        }

        printPokemonStatus(pokemon, player);

        return Command.SINGLE_SUCCESS;
    }
}
