package kiwiapollo.cobblemontrainerbattle.parser.profile;

import com.cobblemon.mod.common.api.abilities.Abilities;
import com.cobblemon.mod.common.api.abilities.Ability;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.Moves;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AbilityDeserializer implements JsonDeserializer<Ability> {
    @Override
    public Ability deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        try {
            return Abilities.INSTANCE.get(json.getAsString()).create(true);
        } catch (NullPointerException e) {
            return null;
        }
    }
}