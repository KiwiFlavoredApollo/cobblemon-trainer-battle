package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class GalarSoundEvent {
    public static final List<SoundEvent> all = new ArrayList<>();

    public static final SoundEvent GALAR_DEFAULT = register("battle.galar.default");
    public static final SoundEvent LEADER_MILO = register("battle.leader.milo");
    public static final SoundEvent LEADER_NESSA = register("battle.leader.nessa");
    public static final SoundEvent LEADER_KABU = register("battle.leader.kabu");
    public static final SoundEvent LEADER_BEA = register("battle.leader.bea");
    public static final SoundEvent LEADER_ALLISTER = register("battle.leader.allister");
    public static final SoundEvent LEADER_OPAL = register("battle.leader.opal");
    public static final SoundEvent LEADER_GORDIE = register("battle.leader.gordie");
    public static final SoundEvent LEADER_MELONY = register("battle.leader.melony");
    public static final SoundEvent LEADER_PIERS = register("battle.leader.piers");
    public static final SoundEvent LEADER_RAIHAN = register("battle.leader.raihan");
    public static final SoundEvent CHAMPION_LEON = register("battle.champion.leon");

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
