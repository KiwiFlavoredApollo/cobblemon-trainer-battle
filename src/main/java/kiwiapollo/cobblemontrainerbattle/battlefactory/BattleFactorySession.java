package kiwiapollo.cobblemontrainerbattle.battlefactory;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.util.Identifier;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class BattleFactorySession {
    public final UUID uuid;

    public List<Identifier> trainersToDefeat;
    public int defeatedTrainerCount;
    public List<Pokemon> partyPokemons;
    public List<Pokemon> tradeablePokemons;
    public Instant timestamp;
    public boolean isDefeated;
    @ Deprecated
    public boolean isTradedPokemon;

    public BattleFactorySession(List<Identifier> trainersToDefeat) {
        this.uuid = UUID.randomUUID();

        this.trainersToDefeat = trainersToDefeat;
        this.defeatedTrainerCount = 0;
        this.partyPokemons = List.of(
                PokemonSpecies.INSTANCE.random().create(BattleFactory.LEVEL),
                PokemonSpecies.INSTANCE.random().create(BattleFactory.LEVEL),
                PokemonSpecies.INSTANCE.random().create(BattleFactory.LEVEL)
        );
        this.tradeablePokemons = List.of();
        this.timestamp = Instant.now();
        this.isDefeated = false;
        this.isTradedPokemon = false;
    }
}
