package kiwiapollo.cobblemontrainerbattle.command.executor;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.common.RentalBattle;
import kiwiapollo.cobblemontrainerbattle.parser.player.BattleContextStorage;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class RandomRentalPokemonProvider implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();

            PartyStore rentalPokemon = new PartyStore(player.getUuid());

            for (int i = 0; i < RentalBattle.PARTY_SIZE; i++) {
//                rentalPokemon.add(PokemonSpecies.INSTANCE.random().create(RentalBattle.LEVEL));
                rentalPokemon.add(PokemonSpecies.INSTANCE.random().create(100));
            }

            BattleContextStorage.getInstance().getOrCreate(player.getUuid()).setRentalPokemon(rentalPokemon);

            return Command.SINGLE_SUCCESS;

        } catch (CommandSyntaxException e) {
            return 0;
        }
    }
}
