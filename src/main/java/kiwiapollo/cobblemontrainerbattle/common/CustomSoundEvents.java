package kiwiapollo.cobblemontrainerbattle.common;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.Arrays;

public class CustomSoundEvents {
    public static void register() {
        Arrays.stream(BattleSounds.values()).forEach(sound -> {
            Registry.register(Registries.SOUND_EVENT, sound.getIdentifier(), sound.getSoundEvent());
        });
    }
}
