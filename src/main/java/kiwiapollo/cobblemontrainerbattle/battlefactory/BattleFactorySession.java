package kiwiapollo.cobblemontrainerbattle.battlefactory;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class BattleFactorySession {
    public final UUID uuid;

    public UUID battleUuid;
    public List<Trainer> trainersToDefeat;
    public int defeatedTrainerCount;
    public List<Pokemon> partyPokemons;
    public Instant timestamp;
    public boolean isDefeated;
    public boolean isTradedPokemon;

    public BattleFactorySession(List<Trainer> trainersToDefeat) {
        this.uuid = UUID.randomUUID();

        this.battleUuid = null;
        this.trainersToDefeat = trainersToDefeat;
        this.defeatedTrainerCount = 0;
        this.partyPokemons = List.of(
                PokemonSpecies.INSTANCE.random().create(BattleFactory.LEVEL),
                PokemonSpecies.INSTANCE.random().create(BattleFactory.LEVEL),
                PokemonSpecies.INSTANCE.random().create(BattleFactory.LEVEL)
        );
        this.timestamp = Instant.now();
        this.isDefeated = false;
        this.isTradedPokemon = false;
    }
}
