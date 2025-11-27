package kiwiapollo.cobblemontrainerbattle.command.rentalpokemon;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.battle.RentalBattle;
import kiwiapollo.cobblemontrainerbattle.battle.RentalPokemonStorage;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Objects;

public class CloneRentalPokemonProvider implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();

            if (!isAtLeastMinimumPartySize(player)) {
                player.sendMessage(getMinimumPartySizeErrorMessage());
                return 0;
            }

            List<Pokemon> clone = Cobblemon.INSTANCE.getStorage().getParty(player).toGappyList().stream()
                    .filter(Objects::nonNull)
                    .map(pokemon -> pokemon.clone(true, true)).toList();

            RentalPokemonStorage.getInstance().get(player).setFirst(clone.get(0));
            RentalPokemonStorage.getInstance().get(player).setSecond(clone.get(1));
            RentalPokemonStorage.getInstance().get(player).setThird(clone.get(2));

            new RentalPokemonStatusPrinter().run(context);

            return Command.SINGLE_SUCCESS;

        } catch (CommandSyntaxException e) {
            return 0;
        }
    }

    private boolean isAtLeastMinimumPartySize(ServerPlayerEntity player) {
        return Cobblemon.INSTANCE.getStorage().getParty(player).occupied() >= RentalBattle.POKEMON_COUNT;
    }

    private MutableText getMinimumPartySizeErrorMessage() {
        return Text.translatable("commands.cobblemontrainerbattle.rentalpokemon.clone.failed.minimum_party_size", RentalBattle.POKEMON_COUNT).formatted(Formatting.RED);
    }
}
