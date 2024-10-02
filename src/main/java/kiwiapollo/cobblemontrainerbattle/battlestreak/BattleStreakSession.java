package kiwiapollo.cobblemontrainerbattle.battlestreak;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.JsonObject;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BattleStreakSession {
    public final UUID uuid;
    public final JsonObject battleStreak;

    public List<Trainer> defeatedTrainers;
    public List<Pokemon> partyPokemons;

    public BattleStreakSession(JsonObject battleStreak) {
        this.uuid = UUID.randomUUID();
        this.battleStreak = battleStreak;
        this.defeatedTrainers = new ArrayList<>();
    }
}
