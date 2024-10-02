package kiwiapollo.cobblemontrainerbattle.groupbattle;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battlefrontier.RandomPartyPokemonsFactory;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GroupBattleSession {
    public final UUID uuid;
    public final String groupResourcePath;

    public UUID battleUuid;
    public List<Trainer> defeatedTrainers;
    public List<Pokemon> partyPokemons;
    public Instant timestamp;
    public boolean isDefeated;

    public GroupBattleSession(String groupResourcePath) {
        this.uuid = UUID.randomUUID();
        this.groupResourcePath = groupResourcePath;

        this.battleUuid = null;
        this.defeatedTrainers = new ArrayList<>();
        this.partyPokemons = new RandomPartyPokemonsFactory().create();
        this.timestamp = Instant.now();
        this.isDefeated = false;
    }
}
