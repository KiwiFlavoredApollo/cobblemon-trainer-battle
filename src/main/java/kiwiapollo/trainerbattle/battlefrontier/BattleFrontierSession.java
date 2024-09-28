package kiwiapollo.trainerbattle.battlefrontier;

import com.cobblemon.mod.common.pokemon.Pokemon;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BattleFrontierSession {
    public final UUID uuid;
    public final BattleFrontierDifficulty difficulty;

    public int battleCount;
    public List<String> defeatedTrainers;
    public List<Pokemon> partyPokemons;
    public Instant timestamp;

    public List<Pokemon> startingPokemons;

    public BattleFrontierSession() {
        this.uuid = UUID.randomUUID();
        this.difficulty = BattleFrontierDifficulty.NORMAL;

        this.battleCount = 0;
        this.defeatedTrainers = new ArrayList<>();
        this.partyPokemons = new ArrayList<>();
        this.timestamp = Instant.now();

        this.startingPokemons = new StartingPokemonsFactory().create();
    }
}
