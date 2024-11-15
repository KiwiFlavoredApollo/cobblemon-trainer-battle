package kiwiapollo.cobblemontrainerbattle.parser.profile;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProfileRegistry<T> {
    private final Map<Identifier, T> profiles = new HashMap<>();

    public void clear() {
        profiles.clear();
    }

    public T put(Identifier identifier, T profile) {
        return profiles.put(identifier, profile);
    }

    public T get(Identifier identifier) {
        return profiles.get(identifier);
    }

    public boolean containsKey(Identifier identifier) {
        return profiles.containsKey(identifier);
    }

    public Set<Identifier> keySet() {
        return profiles.keySet();
    }
}
