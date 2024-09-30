package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collections;

public class ThreePokemonTotalRandomTrainerFactory {
    public Trainer create(ServerPlayerEntity player) {
        int partySize = 3;

        Trainer trainer = new TotalRandomTrainerFactory().create(player);
        while (trainer.pokemons.size() < partySize) {
            trainer = new TotalRandomTrainerFactory().create(player);
        }

        Collections.shuffle(trainer.pokemons);
        trainer.pokemons = trainer.pokemons.subList(0, partySize);
        return trainer;
    }
}
