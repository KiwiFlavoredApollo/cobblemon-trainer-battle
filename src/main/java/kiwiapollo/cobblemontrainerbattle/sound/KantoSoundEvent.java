package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class KantoSoundEvent {
    public static final List<SoundEvent> all = new ArrayList<>();

    public static final SoundEvent KANTO_DEFAULT = registerOrIgnore("battle.kanto.default");
    public static final SoundEvent TRAINER_RED = registerOrIgnore("battle.trainer.red");
    public static final SoundEvent TRAINER_LEAF = registerOrIgnore("battle.trainer.leaf");
    public static final SoundEvent LEADER_BROCK = registerOrIgnore("battle.leader.brock");
    public static final SoundEvent LEADER_MISTY = registerOrIgnore("battle.leader.misty");
    public static final SoundEvent LEADER_LT_SURGE = registerOrIgnore("battle.leader.lt_surge");
    public static final SoundEvent LEADER_ERIKA = registerOrIgnore("battle.leader.erika");
    public static final SoundEvent LEADER_KOGA = registerOrIgnore("battle.leader.koga");
    public static final SoundEvent LEADER_SABRINA = registerOrIgnore("battle.leader.sabrina");
    public static final SoundEvent LEADER_BLAINE = registerOrIgnore("battle.leader.blaine");
    public static final SoundEvent LEADER_GIOVANNI = registerOrIgnore("battle.leader.giovanni");
    public static final SoundEvent ELITE_LORELEI = registerOrIgnore("battle.elite.lorelei");
    public static final SoundEvent ELITE_BRUNO = registerOrIgnore("battle.elite.bruno");
    public static final SoundEvent ELITE_AGATHA = registerOrIgnore("battle.elite.agatha");
    public static final SoundEvent ELITE_LANCE = registerOrIgnore("battle.elite.lance");
    public static final SoundEvent CHAMPION_TERRY = registerOrIgnore("battle.champion.terry");

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
