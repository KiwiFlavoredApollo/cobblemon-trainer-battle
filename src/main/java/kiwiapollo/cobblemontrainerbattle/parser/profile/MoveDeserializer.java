package kiwiapollo.cobblemontrainerbattle.parser.profile;

import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.Moves;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.FormData;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MoveDeserializer implements JsonDeserializer<Move> {
    @Override
    public Move deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        try {
            return Moves.INSTANCE.getByName(json.getAsString()).create();
        } catch (NullPointerException e) {
            return null;
        }
    }
}