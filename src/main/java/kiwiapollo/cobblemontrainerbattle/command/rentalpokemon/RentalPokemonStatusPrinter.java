package kiwiapollo.cobblemontrainerbattle.command.rentalpokemon;

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

        if (!isRentalPokemonExist(pokemon)) {
            player.sendMessage(getNoRentalPokemonErrorMessage());
            return 0;
        }

        print(pokemon, player);

        return Command.SINGLE_SUCCESS;
    }

    private boolean isRentalPokemonExist(RentalPokemon pokemon) {
        return pokemon.occupied() != 0;
    }

    private Text getNoRentalPokemonErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.rentalpokemon.showrental.failed.no_rental_pokemon").formatted(Formatting.RED);
    }
}
