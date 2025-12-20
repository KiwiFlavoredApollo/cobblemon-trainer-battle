package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class UnovaWhiteSoundEvent {
    public static final List<SoundEvent> all = new ArrayList<>();

    public static final SoundEvent UNOVA_DEFAULT = register("battle.unova.default");
    public static final SoundEvent LEADER_CHEREN = register("battle.leader.cheren");
    public static final SoundEvent LEADER_ROXIE = register("battle.leader.roxie");
    public static final SoundEvent LEADER_BURGH = register("battle.leader.burgh");
    public static final SoundEvent LEADER_ELESA = register("battle.leader.elesa");
    public static final SoundEvent LEADER_CLAY = register("battle.leader.clay");
    public static final SoundEvent LEADER_SKYLA = register("battle.leader.skyla");
    public static final SoundEvent LEADER_DRAYDEN = register("battle.leader.drayden");
    public static final SoundEvent LEADER_MARLON = register("battle.leader.marlon");
    public static final SoundEvent ELITE_SHAUNTAL = register("battle.elite.shauntal");
    public static final SoundEvent ELITE_MARSHAL = register("battle.elite.marshal");
    public static final SoundEvent ELITE_GRIMSLEY = register("battle.elite.grimsley");
    public static final SoundEvent ELITE_CAITLIN = register("battle.elite.caitlin");
    public static final SoundEvent CHAMPION_IRIS = register("battle.champion.iris");

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
