package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class HoenSoundEvent {
    public static final List<SoundEvent> all = new ArrayList<>();

    public static final SoundEvent HOENN_DEFAULT = registerOrIgnore("battle.hoenn.default");
    public static final SoundEvent LEADER_ROXANNE = registerOrIgnore("battle.leader.roxanne");
    public static final SoundEvent LEADER_BRAWLY = registerOrIgnore("battle.leader.brawly");
    public static final SoundEvent LEADER_WATTSON = registerOrIgnore("battle.leader.wattson");
    public static final SoundEvent LEADER_FLANNERY = registerOrIgnore("battle.leader.flannery");
    public static final SoundEvent LEADER_NORMAN = registerOrIgnore("battle.leader.norman");
    public static final SoundEvent LEADER_WINONA = registerOrIgnore("battle.leader.winona");
    public static final SoundEvent LEADER_TATE_AND_LIZA = registerOrIgnore("battle.leader.tate_and_liza");
    public static final SoundEvent LEADER_JUAN = registerOrIgnore("battle.leader.juan");
    public static final SoundEvent ELITE_SIDNEY = registerOrIgnore("battle.elite.sidney");
    public static final SoundEvent ELITE_PHOEBE = registerOrIgnore("battle.elite.phoebe");
    public static final SoundEvent ELITE_GLACIA = registerOrIgnore("battle.elite.glacia");
    public static final SoundEvent ELITE_DRAKE = registerOrIgnore("battle.elite.drake");
    public static final SoundEvent CHAMPION_WALLACE = registerOrIgnore("battle.champion.wallace");

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
