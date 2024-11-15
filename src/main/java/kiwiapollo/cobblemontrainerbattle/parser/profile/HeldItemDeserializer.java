package kiwiapollo.cobblemontrainerbattle.parser.profile;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.lang.reflect.Type;
import java.util.Objects;

public class HeldItemDeserializer implements JsonDeserializer<ItemStack> {
    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        try {
            Identifier item = Objects.requireNonNull(Identifier.tryParse(json.getAsString()));
            return new ItemStack(Registries.ITEM.get(item));
        } catch (NullPointerException e) {
            return null;
        }
    }
}