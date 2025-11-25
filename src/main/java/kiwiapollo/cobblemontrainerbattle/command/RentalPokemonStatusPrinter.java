package kiwiapollo.cobblemontrainerbattle.command;

import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.context.RentalPokemon;
import kiwiapollo.cobblemontrainerbattle.context.RentalPokemonStorage;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class RentalPokemonStatusPrinter extends PokemonStatusPrinter implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        PartyStore rental = getRentalPokemon(player);

        if (rental.occupied() == 0) {
            player.sendMessage(Text.translatable("command.cobblemontrainerbattle.error.rentalpokemon.rental_pokemon_not_exist").formatted(Formatting.RED));
            return 0;
        }

        printPokemonStatus(rental, player);

        return Command.SINGLE_SUCCESS;
    }

    private static PartyStore getRentalPokemon(ServerPlayerEntity player) {
        RentalPokemon rental = RentalPokemonStorage.getInstance().get(player);
        PartyStore party = new PartyStore(player.getUuid());
        rental.stream().forEach(party::add);

        return party;
    }
}
