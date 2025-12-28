package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class GalarSoundEvent {
    public static final List<SoundEvent> all = new ArrayList<>();

    public static final SoundEvent GALAR_DEFAULT = registerOrIgnore("battle.galar.default");
    public static final SoundEvent LEADER_MILO = registerOrIgnore("battle.leader.milo");
    public static final SoundEvent LEADER_NESSA = registerOrIgnore("battle.leader.nessa");
    public static final SoundEvent LEADER_KABU = registerOrIgnore("battle.leader.kabu");
    public static final SoundEvent LEADER_BEA = registerOrIgnore("battle.leader.bea");
    public static final SoundEvent LEADER_ALLISTER = registerOrIgnore("battle.leader.allister");
    public static final SoundEvent LEADER_OPAL = registerOrIgnore("battle.leader.opal");
    public static final SoundEvent LEADER_GORDIE = registerOrIgnore("battle.leader.gordie");
    public static final SoundEvent LEADER_MELONY = registerOrIgnore("battle.leader.melony");
    public static final SoundEvent LEADER_PIERS = registerOrIgnore("battle.leader.piers");
    public static final SoundEvent LEADER_RAIHAN = registerOrIgnore("battle.leader.raihan");
    public static final SoundEvent CHAMPION_LEON = registerOrIgnore("battle.champion.leon");

    public static void initialize() {

    }

    private static SoundEvent registerOrIgnore(String name) {
        if (!isRegistered(name)) {
            return register(name);

        } else {
            return ignore(name);
        }
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

    private static boolean isRegistered(String name) {
        return Registries.SOUND_EVENT.containsId(Identifier.of(CobblemonTrainerBattle.MOD_ID, name));
    }

    public static List<SoundEvent> getAll() {
        return new ArrayList<>(all);
    }
}
