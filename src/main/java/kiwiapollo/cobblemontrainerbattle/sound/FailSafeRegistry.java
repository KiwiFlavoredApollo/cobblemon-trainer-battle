package kiwiapollo.cobblemontrainerbattle.sound;

import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FailSafeRegistry {
    static <V, T extends V> T register(Registry<V> registry, Identifier id, T entry) {
        try {
            return Registry.register(registry, id, entry);

        } catch (RuntimeException ignored) {
            return entry;
        }
    }
}
