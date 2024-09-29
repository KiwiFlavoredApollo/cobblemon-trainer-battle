package kiwiapollo.cobblemontrainerbattle.battlefrontier;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.common.Trainer;

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
    public boolean isRerolled;

    public BattleFrontierSession() {
        this.uuid = UUID.randomUUID();
        this.difficulty = BattleFrontierDifficulty.NORMAL;

        this.battleUuid = null;
        this.battleCount = 0;
        this.defeatedTrainers = new ArrayList<>();
        this.partyPokemons = new RandomPartyPokemonsFactory().create();
        this.timestamp = Instant.now();
        this.isDefeated = false;
        this.isRerolled = false;
    }
}
