package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.util.Identifier;

public class TrainerFile {
    public final JsonArray pokemons;
    public final JsonObject configuration;

    public TrainerFile(JsonArray pokemons, JsonObject configuration) {
        this.pokemons = pokemons;
        this.configuration = configuration;
    }
}
