package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class PaldeaSoundEvent {
    public static final List<SoundEvent> all = new ArrayList<>();

    public static final SoundEvent PALDEA_DEFAULT = registerOrIgnore("battle.paldea.default");
    public static final SoundEvent LEADER_KATY = registerOrIgnore("battle.leader.katy");
    public static final SoundEvent LEADER_BRASSIUS = registerOrIgnore("battle.leader.brassius");
    public static final SoundEvent LEADER_IONO = registerOrIgnore("battle.leader.iono");
    public static final SoundEvent LEADER_KOFU = registerOrIgnore("battle.leader.kofu");
    public static final SoundEvent LEADER_LARRY = registerOrIgnore("battle.leader.larry");
    public static final SoundEvent LEADER_RYME = registerOrIgnore("battle.leader.ryme");
    public static final SoundEvent LEADER_TULIP = registerOrIgnore("battle.leader.tulip");
    public static final SoundEvent LEADER_GRUSHA = registerOrIgnore("battle.leader.grusha");
    public static final SoundEvent ELITE_RIKA = registerOrIgnore("battle.elite.rika");
    public static final SoundEvent ELITE_POPPY = registerOrIgnore("battle.elite.poppy");
    public static final SoundEvent ELITE_LARRY = registerOrIgnore("battle.elite.larry");
    public static final SoundEvent ELITE_HASSEL = registerOrIgnore("battle.elite.hassel");
    public static final SoundEvent CHAMPION_GEETA = registerOrIgnore("battle.champion.geeta");
    public static final SoundEvent ELITE_CRISPIN = registerOrIgnore("battle.elite.crispin");
    public static final SoundEvent ELITE_AMARYS = registerOrIgnore("battle.elite.amarys");
    public static final SoundEvent ELITE_LACEY = registerOrIgnore("battle.elite.lacey");
    public static final SoundEvent ELITE_DRAYTON = registerOrIgnore("battle.elite.drayton");
    public static final SoundEvent CHAMPION_KIERAN = registerOrIgnore("battle.champion.kieran");

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
