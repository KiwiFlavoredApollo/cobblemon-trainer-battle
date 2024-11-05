package kiwiapollo.cobblemontrainerbattle.parser;

import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerProfile;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TrainerProfileStorage {
    private static final Map<Identifier, TrainerProfile> profiles = new HashMap<>();

    public static void clear() {
        profiles.clear();
    }

    public static void put(Identifier identifier, TrainerProfile profile) {
        profiles.put(identifier, profile);
    }

    public static TrainerProfile get(Identifier identifier) {
        return profiles.get(identifier);
    }

    public static boolean containsKey(Identifier identifier) {
        return profiles.containsKey(identifier);
    }

    public static Set<Identifier> keySet() {
        return profiles.keySet();
    }
}
