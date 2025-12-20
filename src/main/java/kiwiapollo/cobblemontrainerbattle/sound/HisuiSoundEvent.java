package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class HisuiSoundEvent {
    public static final List<SoundEvent> all = new ArrayList<>();

    public static final SoundEvent HISUI_DEFAULT = register("battle.hisui.default");

    public static void initialize() {

    }

    private static SoundEvent register(String name) {
        Identifier identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, name);
        SoundEvent registered = FailSafeRegistry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
        all.add(registered);

        return registered;
    }

    public static List<SoundEvent> getAll() {
        return new ArrayList<>(all);
    }
}
