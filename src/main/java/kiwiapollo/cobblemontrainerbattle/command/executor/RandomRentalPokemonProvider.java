package kiwiapollo.cobblemontrainerbattle.command.executor;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.RentalBattle;
import kiwiapollo.cobblemontrainerbattle.global.context.RentalPokemonStorage;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class RandomRentalPokemonProvider implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();

            RentalPokemonStorage.getInstance().get(player).setFirst(PokemonSpecies.INSTANCE.random().create(RentalBattle.LEVEL));
            RentalPokemonStorage.getInstance().get(player).setSecond(PokemonSpecies.INSTANCE.random().create(RentalBattle.LEVEL));
            RentalPokemonStorage.getInstance().get(player).setThird(PokemonSpecies.INSTANCE.random().create(RentalBattle.LEVEL));

            new RentalPokemonStatusPrinter().run(context);

            return Command.SINGLE_SUCCESS;

        } catch (CommandSyntaxException e) {
            return 0;
        }
    }
}
