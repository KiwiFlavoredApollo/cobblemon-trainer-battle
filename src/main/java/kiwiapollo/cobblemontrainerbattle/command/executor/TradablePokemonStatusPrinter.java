package kiwiapollo.cobblemontrainerbattle.command.executor;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.parser.player.BattleContextStorage;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.Objects;

public class TradablePokemonStatusPrinter extends PokemonStatusPrinter {
    @Override
    protected List<Pokemon> getPokemons(ServerPlayerEntity player) {
        return BattleContextStorage.getInstance().getOrCreate(player.getUuid()).getTradablePokemon().toGappyList().stream().filter(Objects::nonNull).toList();
    }
}
