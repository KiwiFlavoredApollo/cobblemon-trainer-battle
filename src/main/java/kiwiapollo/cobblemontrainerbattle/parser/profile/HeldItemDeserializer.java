package kiwiapollo.cobblemontrainerbattle.parser.profile;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;
import java.util.NoSuchElementException;

public class HeldItemDeserializer implements JsonDeserializer<ItemStack> {
    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        try {
            return new ItemStack(Registries.ITEM.getOrEmpty(Identifier.tryParse(json.getAsString())).get());
        } catch (NoSuchElementException e) {
            throw new JsonParseException(e);
        }
    }
}