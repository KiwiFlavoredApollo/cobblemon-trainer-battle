package kiwiapollo.cobblemontrainerbattle.parser.profile;

import kiwiapollo.cobblemontrainerbattle.groupbattle.TrainerGroupProfile;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TrainerGroupProfileStorage {
    private static final Map<Identifier, TrainerGroupProfile> profiles = new HashMap<>();

    public static void clear() {
        profiles.clear();
    }

    public static void put(Identifier identifier, TrainerGroupProfile profile) {
        profiles.put(identifier, profile);
    }

    public static TrainerGroupProfile get(Identifier identifier) {
        return profiles.get(identifier);
    }

    public static boolean containsKey(Identifier identifier) {
        return profiles.containsKey(identifier);
    }

    public static Set<Identifier> keySet() {
        return profiles.keySet();
    }
}
