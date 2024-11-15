package kiwiapollo.cobblemontrainerbattle.parser.profile;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.FormData;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;
import java.util.NoSuchElementException;

public class PokemonDeserializer implements JsonDeserializer<Pokemon> {
    @Override
    public Pokemon deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        try {
            Identifier species = Identifier.tryParse(json.getAsJsonObject().get("species").getAsString());
            FormData form = getForm(species, json.getAsJsonObject().get("form").getAsString());

            Pokemon pokemon = PokemonSpecies.INSTANCE.getByIdentifier(species).create(10);
            pokemon.setForm(form);

            return pokemon;
        } catch (NullPointerException | NoSuchElementException e) {
            return null;
        }
    }
    private FormData getForm(Identifier species, String form) {
        return PokemonSpecies.INSTANCE.getByIdentifier(species).getForms().stream()
                .filter(formData -> form.equals(formData.getName()))
                .findFirst().get();
    }
}