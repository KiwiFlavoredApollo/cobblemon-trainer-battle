package kiwiapollo.cobblemontrainerbattle.command.executor;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.battle.preset.RentalBattlePreset;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContextStorage;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class RandomRentalPokemonProvider implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();

            PartyStore rentalPokemon = new PartyStore(player.getUuid());

            for (int i = 0; i < RentalBattlePreset.PARTY_SIZE; i++) {
                rentalPokemon.add(PokemonSpecies.INSTANCE.random().create(RentalBattlePreset.LEVEL));
            }

            BattleContextStorage.getInstance().getOrCreate(player.getUuid()).setRentalPokemon(rentalPokemon);

            new RentalPokemonStatusPrinter().run(context);

            return Command.SINGLE_SUCCESS;

        } catch (CommandSyntaxException e) {
            return 0;
        }
    }
}
