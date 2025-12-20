package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class JohtoSoundEvent {
    public static final List<SoundEvent> all = new ArrayList<>();

    public static SoundEvent JOHTO_DEFAULT = register("battle.johto.default");
    public static SoundEvent TRAINER_EUSINE = register("battle.trainer.eusine");
    public static SoundEvent TRAINER_SILVER = register("battle.trainer.silver");
    public static SoundEvent LEADER_FALKNER = register("battle.leader.falkner");
    public static SoundEvent LEADER_BUGSY = register("battle.leader.bugsy");
    public static SoundEvent LEADER_WHITNEY = register("battle.leader.whitney");
    public static SoundEvent LEADER_MORTY = register("battle.leader.morty");
    public static SoundEvent LEADER_CHUCK = register("battle.leader.chuck");
    public static SoundEvent LEADER_JASMINE = register("battle.leader.jasmine");
    public static SoundEvent LEADER_PRYCE = register("battle.leader.pryce");
    public static SoundEvent LEADER_CLAIR = register("battle.leader.clair");
    public static SoundEvent ELITE_WILL = register("battle.elite.will");
    public static SoundEvent ELITE_KOGA = register("battle.elite.koga");
    public static SoundEvent ELITE_BRUNO = register("battle.elite.bruno");
    public static SoundEvent ELITE_KAREN = register("battle.elite.karen");
    public static SoundEvent CHAMPION_LANCE = register("battle.champion.lance");

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
