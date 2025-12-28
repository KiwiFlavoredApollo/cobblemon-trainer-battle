package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class AlolaSoundEvent {
    public static final List<SoundEvent> all = new ArrayList<>();

    public static final SoundEvent ALOLA_DEFAULT = registerOrIgnore("battle.alola.default");
    public static final SoundEvent LEADER_ILIMA = registerOrIgnore("battle.leader.ilima");
    public static final SoundEvent LEADER_LANA = registerOrIgnore("battle.leader.lana");
    public static final SoundEvent LEADER_KIAWE = registerOrIgnore("battle.leader.kiawe");
    public static final SoundEvent LEADER_MALLOW = registerOrIgnore("battle.leader.mallow");
    public static final SoundEvent LEADER_SOPHOCLES = registerOrIgnore("battle.leader.sophocles");
    public static final SoundEvent LEADER_MINA = registerOrIgnore("battle.leader.mina");
    public static final SoundEvent LEADER_ACEROLA = registerOrIgnore("battle.leader.acerola");
    public static final SoundEvent ELITE_HALA = registerOrIgnore("battle.elite.hala");
    public static final SoundEvent ELITE_ACEROLA = registerOrIgnore("battle.elite.acerola");
    public static final SoundEvent ELITE_KAHILI = registerOrIgnore("battle.elite.kahili");
    public static final SoundEvent ELITE_MOLAYNE = registerOrIgnore("battle.elite.molayne");
    public static final SoundEvent CHAMPION_KUKUI = registerOrIgnore("battle.champion.kukui");

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
