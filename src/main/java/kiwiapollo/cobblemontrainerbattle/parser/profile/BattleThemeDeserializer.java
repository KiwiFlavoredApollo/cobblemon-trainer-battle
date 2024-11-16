package kiwiapollo.cobblemontrainerbattle.parser.profile;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;

public class BattleThemeDeserializer implements JsonDeserializer<SoundEvent> {
    @Override
    public SoundEvent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return SoundEvent.of(Identifier.tryParse(json.getAsString()));
    }
}

