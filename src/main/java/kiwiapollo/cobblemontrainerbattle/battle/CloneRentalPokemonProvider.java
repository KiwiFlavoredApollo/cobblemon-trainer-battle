package kiwiapollo.cobblemontrainerbattle.battle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.MinimumPartySizePredicate;
import kiwiapollo.cobblemontrainerbattle.common.RentalBattle;
import kiwiapollo.cobblemontrainerbattle.parser.player.BattleContextStorage;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

public class CloneRentalPokemonProvider implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            ServerPlayerEntity player = context.getSource().getPlayerOrThrow();

            if (!hasMinimumPartySize(player)) {
                player.sendMessage(Text.translatable("You must have minimum party size of 3"));
                return 0;
            }

            PartyStore original = Cobblemon.INSTANCE.getStorage().getParty(player);
            PartyStore clone = toClone(original);
            PartyStore rental = toRental(clone);

            BattleContextStorage.getInstance().getOrCreate(player.getUuid()).setRentalPokemon(rental);

            return Command.SINGLE_SUCCESS;

        } catch (CommandSyntaxException e) {
            return 0;
        }
    }

    private PartyStore toClone(PartyStore party) {
        PartyStore clone = new PartyStore(UUID.randomUUID());

        party.toGappyList().stream().filter(Objects::nonNull).forEach(pokemon -> clone.add(pokemon.clone(true, true)));

        return clone;
    }

    private PartyStore toRental(PartyStore party) {
        PartyStore rental = new PartyStore(UUID.randomUUID());

        party.toGappyList().stream().filter(Objects::nonNull).toList().subList(0, RentalBattle.PARTY_SIZE).forEach(rental::add);
        rental.toGappyList().forEach(pokemon -> pokemon.setLevel(RentalBattle.LEVEL));

        return rental;
    }

    // TODO
    private boolean hasMinimumPartySize(ServerPlayerEntity player) {
        Predicate<Integer> minimumPartySizePredicate = new MinimumPartySizePredicate.BattleFormatPredicate(RentalBattle.PARTY_SIZE);
        PlayerPartyStore original = Cobblemon.INSTANCE.getStorage().getParty(player);
        return minimumPartySizePredicate.test(original.occupied());
    }
}
