package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class JohtoSoundEvent {
    public static final List<SoundEvent> all = new ArrayList<>();

    public static SoundEvent JOHTO_DEFAULT = registerOrIgnore("battle.johto.default");
    public static SoundEvent TRAINER_EUSINE = registerOrIgnore("battle.trainer.eusine");
    public static SoundEvent TRAINER_SILVER = registerOrIgnore("battle.trainer.silver");
    public static SoundEvent LEADER_FALKNER = registerOrIgnore("battle.leader.falkner");
    public static SoundEvent LEADER_BUGSY = registerOrIgnore("battle.leader.bugsy");
    public static SoundEvent LEADER_WHITNEY = registerOrIgnore("battle.leader.whitney");
    public static SoundEvent LEADER_MORTY = registerOrIgnore("battle.leader.morty");
    public static SoundEvent LEADER_CHUCK = registerOrIgnore("battle.leader.chuck");
    public static SoundEvent LEADER_JASMINE = registerOrIgnore("battle.leader.jasmine");
    public static SoundEvent LEADER_PRYCE = registerOrIgnore("battle.leader.pryce");
    public static SoundEvent LEADER_CLAIR = registerOrIgnore("battle.leader.clair");
    public static SoundEvent ELITE_WILL = registerOrIgnore("battle.elite.will");
    public static SoundEvent ELITE_KOGA = registerOrIgnore("battle.elite.koga");
    public static SoundEvent ELITE_BRUNO = registerOrIgnore("battle.elite.bruno");
    public static SoundEvent ELITE_KAREN = registerOrIgnore("battle.elite.karen");
    public static SoundEvent CHAMPION_LANCE = registerOrIgnore("battle.champion.lance");

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
