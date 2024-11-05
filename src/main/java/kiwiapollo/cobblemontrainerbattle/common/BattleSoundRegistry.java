package kiwiapollo.cobblemontrainerbattle.common;

import com.cobblemon.mod.common.platform.PlatformRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundEvent;
import org.jetbrains.annotations.NotNull;

public class BattleSoundRegistry extends PlatformRegistry<Registry<SoundEvent>, RegistryKey<Registry<SoundEvent>>, SoundEvent> {

    @Override
    public @NotNull Registry<SoundEvent> getRegistry() {
        return null;
    }

    @Override
    public @NotNull RegistryKey<Registry<SoundEvent>> getRegistryKey() {
        return null;
    }
}
