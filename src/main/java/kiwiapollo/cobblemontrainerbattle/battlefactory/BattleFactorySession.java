package kiwiapollo.cobblemontrainerbattle.battlefactory;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BattleFactorySession {
    public final UUID uuid;

    public UUID battleUuid;
    public List<Trainer> defeatedTrainers;
    public List<Pokemon> partyPokemons;
    public Instant timestamp;
    public boolean isDefeated;
    public boolean isTradedPokemon;

    public BattleFactorySession() {
        this.uuid = UUID.randomUUID();

        this.battleUuid = null;
        this.defeatedTrainers = new ArrayList<>();
        this.partyPokemons = new RandomPartyPokemonsFactory().create();
        this.timestamp = Instant.now();
        this.isDefeated = false;
        this.isTradedPokemon = false;
    }
}
