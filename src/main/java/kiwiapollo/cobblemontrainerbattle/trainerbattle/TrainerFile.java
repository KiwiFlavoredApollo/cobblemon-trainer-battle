package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import com.google.gson.JsonArray;
import net.minecraft.util.Identifier;

public class TrainerFile {
    public final Identifier identifier;
    public final JsonArray jsonArray;

    public TrainerFile(Identifier identifier, JsonArray jsonArray) {
        this.identifier = identifier;
        this.jsonArray = jsonArray;
    }
}
