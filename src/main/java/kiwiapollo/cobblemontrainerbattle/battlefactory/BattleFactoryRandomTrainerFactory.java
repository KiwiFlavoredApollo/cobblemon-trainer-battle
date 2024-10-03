package kiwiapollo.cobblemontrainerbattle.battlefactory;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.RandomTrainerFactory;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BattleFactoryRandomTrainerFactory {
    private static final int REQUIRED_POKEMON_COUNT = 3;
    public Trainer create(ServerPlayerEntity player) {
        Trainer trainer = new RandomTrainerFactory().create(player);
        while (trainer.pokemons.size() < REQUIRED_POKEMON_COUNT) {
            trainer = new RandomTrainerFactory().create(player);
        }

        List<Pokemon> pokemons = new ArrayList<>(trainer.pokemons);
        Collections.shuffle(pokemons);
        trainer.pokemons = pokemons.subList(0, REQUIRED_POKEMON_COUNT);

        trainer.pokemons.stream().forEach(pokemon -> pokemon.setLevel(BattleFactory.LEVEL));

        return trainer;
    }
}
