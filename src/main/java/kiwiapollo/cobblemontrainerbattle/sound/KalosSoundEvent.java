package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class KalosSoundEvent {
    public static final List<SoundEvent> all = new ArrayList<>();

    public static final SoundEvent KALOS_DEFAULT = registerOrIgnore("battle.kalos.default");
    public static final SoundEvent LEADER_VIOLA = registerOrIgnore("battle.leader.viola");
    public static final SoundEvent LEADER_GRANT = registerOrIgnore("battle.leader.grant");
    public static final SoundEvent LEADER_KORRINA = registerOrIgnore("battle.leader.korrina");
    public static final SoundEvent LEADER_RAMOS = registerOrIgnore("battle.leader.ramos");
    public static final SoundEvent LEADER_CLEMONT = registerOrIgnore("battle.leader.clemont");
    public static final SoundEvent LEADER_VALERIE = registerOrIgnore("battle.leader.valerie");
    public static final SoundEvent LEADER_OLYMPIA = registerOrIgnore("battle.leader.olympia");
    public static final SoundEvent LEADER_WULFRIC = registerOrIgnore("battle.leader.wulfric");
    public static final SoundEvent ELITE_MALVA = registerOrIgnore("battle.elite.malva");
    public static final SoundEvent ELITE_SIEBOLD = registerOrIgnore("battle.elite.siebold");
    public static final SoundEvent ELITE_WIKSTROM = registerOrIgnore("battle.elite.wikstrom");
    public static final SoundEvent ELITE_DRASNA = registerOrIgnore("battle.elite.drasna");
    public static final SoundEvent CHAMPION_DIANTHA = registerOrIgnore("battle.champion.diantha");

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
