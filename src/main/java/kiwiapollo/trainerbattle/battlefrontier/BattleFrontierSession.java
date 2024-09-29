package kiwiapollo.trainerbattle.battlefrontier;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.trainerbattle.common.Trainer;

import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BattleFrontierSession {
    public final UUID uuid;
    public final BattleFrontierDifficulty difficulty;

    public UUID battleUuid;
    public int battleCount;
    public List<Trainer> defeatedTrainers;
    public List<Pokemon> partyPokemons;
    public Instant timestamp;
    public boolean isDefeated;

    public List<Pokemon> startingPokemons;

    public BattleFrontierSession() {
        this.uuid = UUID.randomUUID();
        this.difficulty = BattleFrontierDifficulty.NORMAL;

        this.battleUuid = null;
        this.battleCount = 0;
        this.defeatedTrainers = new ArrayList<>();
        this.partyPokemons = new ArrayList<>();
        this.timestamp = Instant.now();
        this.isDefeated = false;

        this.startingPokemons = new StartingPokemonsFactory().create();
    }
}
