package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThreePokemonTotalRandomTrainerFactory {
    public Trainer create(ServerPlayerEntity player) {
        int partySize = 3;

        Trainer trainer = new RandomTrainerFactory().create(player);
        while (trainer.pokemons.size() < partySize) {
            trainer = new RandomTrainerFactory().create(player);
        }

        List<Pokemon> pokemons = new ArrayList<>(trainer.pokemons);
        Collections.shuffle(pokemons);
        trainer.pokemons = pokemons.subList(0, partySize);

        return trainer;
    }
}
