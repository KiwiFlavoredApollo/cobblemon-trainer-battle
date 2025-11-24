package kiwiapollo.cobblemontrainerbattle.command.executor;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.global.context.RentalPokemonStorage;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class CloneRentalPokemonProvider implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();

            if (!hasMinimumPartySize(player)) {
                player.sendMessage(Text.translatable("command.cobblemontrainerbattle.error.rentalpokemon.player_minimum_party_size", 3).formatted(Formatting.RED));
                return 0;
            }

            PartyStore original = Cobblemon.INSTANCE.getStorage().getParty(player);
            RentalPokemonStorage.getInstance().get(player).setFirst(original.get(1));
            RentalPokemonStorage.getInstance().get(player).setSecond(original.get(2));
            RentalPokemonStorage.getInstance().get(player).setThird(original.get(3));

            new RentalPokemonStatusPrinter().run(context);

            return Command.SINGLE_SUCCESS;

        } catch (CommandSyntaxException e) {
            return 0;
        }
    }

    // TODO
    private boolean hasMinimumPartySize(ServerPlayerEntity player) {
        return Cobblemon.INSTANCE.getStorage().getParty(player).occupied() >= 3;
    }
}
