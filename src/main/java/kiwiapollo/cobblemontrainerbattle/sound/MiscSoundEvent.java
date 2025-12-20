package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class MiscSoundEvent {
    public static final List<SoundEvent> all = new ArrayList<>();

    public static final SoundEvent LEADER_DEFAULT = register("battle.leader.default");
    public static final SoundEvent ELITE_DEFAULT = register("battle.elite.default");
    public static final SoundEvent CHAMPION_DEFAULT = register("battle.champion.default");

    public static void initialize() {

    }

    private static SoundEvent register(String name) {
        try {
            Identifier identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, name);
            SoundEvent registered = Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
            all.add(registered);

            return registered;
            
        } catch (RuntimeException e) {
            Identifier identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, name);
            SoundEvent sound = SoundEvent.of(identifier);
            all.add(sound);

            return sound;
        }
    }

    public static List<SoundEvent> getAll() {
        return new ArrayList<>(all);
    }
}
