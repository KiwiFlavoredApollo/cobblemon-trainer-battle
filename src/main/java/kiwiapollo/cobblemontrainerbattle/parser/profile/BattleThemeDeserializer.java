package kiwiapollo.cobblemontrainerbattle.parser.profile;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;

public class BattleThemeDeserializer implements JsonDeserializer<SoundEvent> {
    @Override
    public SoundEvent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        String battleTheme = json.getAsString();
        if (battleTheme.contains(":")) {
            return SoundEvent.of(Identifier.tryParse(battleTheme));
        } else {
            return SoundEvent.of(Identifier.of(CobblemonTrainerBattle.MOD_ID, battleTheme));
        }
    }
}

