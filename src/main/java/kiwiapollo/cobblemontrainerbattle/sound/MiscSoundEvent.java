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

    public static final SoundEvent LEADER_DEFAULT = registerOrIgnore("battle.leader.default");
    public static final SoundEvent ELITE_DEFAULT = registerOrIgnore("battle.elite.default");
    public static final SoundEvent CHAMPION_DEFAULT = registerOrIgnore("battle.champion.default");

    public static void initialize() {

    }

    private static SoundEvent registerOrIgnore(String name) {
        if (!isRegistered(name)) {
            return register(name);
        } else {
            return ignore(name);
        }
    }

    private static boolean isRegistered(String name) {
        return Registries.SOUND_EVENT.containsId(Identifier.of(CobblemonTrainerBattle.MOD_ID, name));
    }

    private static SoundEvent register(String name) {
        Identifier identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, name);
        SoundEvent sound = SoundEvent.of(identifier);
        Registry.register(Registries.SOUND_EVENT, identifier, sound);
        all.add(sound);

        return sound;
    }

    private static SoundEvent ignore(String name) {
        Identifier identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, name);
        SoundEvent sound = SoundEvent.of(identifier);
        all.add(sound);

        return sound;
    }

    public static List<SoundEvent> getAll() {
        return new ArrayList<>(all);
    }
}
