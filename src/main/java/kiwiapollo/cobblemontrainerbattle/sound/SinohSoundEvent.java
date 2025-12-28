package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class SinohSoundEvent {
    public static final List<SoundEvent> all = new ArrayList<>();

    public static final SoundEvent SINNOH_DEFAULT = registerOrIgnore("battle.sinnoh.default");
    public static final SoundEvent LEADER_ROARK = registerOrIgnore("battle.leader.roark");
    public static final SoundEvent LEADER_GARDENIA = registerOrIgnore("battle.leader.gardenia");
    public static final SoundEvent LEADER_MAYLENE = registerOrIgnore("battle.leader.maylene");
    public static final SoundEvent LEADER_CRASHER_WAKE = registerOrIgnore("battle.leader.crasher_wake");
    public static final SoundEvent LEADER_FANTINA = registerOrIgnore("battle.leader.fantina");
    public static final SoundEvent LEADER_BYRON = registerOrIgnore("battle.leader.byron");
    public static final SoundEvent LEADER_CANDICE = registerOrIgnore("battle.leader.candice");
    public static final SoundEvent LEADER_VOLKNER = registerOrIgnore("battle.leader.volkner");
    public static final SoundEvent ELITE_AARON = registerOrIgnore("battle.elite.aaron");
    public static final SoundEvent ELITE_BERTHA = registerOrIgnore("battle.elite.bertha");
    public static final SoundEvent ELITE_FLINT = registerOrIgnore("battle.elite.flint");
    public static final SoundEvent ELITE_LUCIAN = registerOrIgnore("battle.elite.lucian");
    public static final SoundEvent CHAMPION_CYNTHIA = registerOrIgnore("battle.champion.cynthia");

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
