package kiwiapollo.cobblemontrainerbattle.command.executor;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.global.context.BattleContextStorage;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.Objects;

public class RentalPokemonStatusPrinter extends PokemonStatusPrinter {
    @Override
    protected List<Pokemon> getPokemons(ServerPlayerEntity player) {
        return BattleContextStorage.getInstance().getOrCreate(player.getUuid()).getRentalPokemon().toGappyList().stream().filter(Objects::nonNull).toList();
    }
}
