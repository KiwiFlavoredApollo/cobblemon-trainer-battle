package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class PaldeaSoundEvent {
    public static final List<SoundEvent> all = new ArrayList<>();

    public static final SoundEvent PALDEA_DEFAULT = register("battle.paldea.default");
    public static final SoundEvent LEADER_KATY = register("battle.leader.katy");
    public static final SoundEvent LEADER_BRASSIUS = register("battle.leader.brassius");
    public static final SoundEvent LEADER_IONO = register("battle.leader.iono");
    public static final SoundEvent LEADER_KOFU = register("battle.leader.kofu");
    public static final SoundEvent LEADER_LARRY = register("battle.leader.larry");
    public static final SoundEvent LEADER_RYME = register("battle.leader.ryme");
    public static final SoundEvent LEADER_TULIP = register("battle.leader.tulip");
    public static final SoundEvent LEADER_GRUSHA = register("battle.leader.grusha");
    public static final SoundEvent ELITE_RIKA = register("battle.elite.rika");
    public static final SoundEvent ELITE_POPPY = register("battle.elite.poppy");
    public static final SoundEvent ELITE_LARRY = register("battle.elite.larry");
    public static final SoundEvent ELITE_HASSEL = register("battle.elite.hassel");
    public static final SoundEvent CHAMPION_GEETA = register("battle.champion.geeta");
    public static final SoundEvent ELITE_CRISPIN = register("battle.elite.crispin");
    public static final SoundEvent ELITE_AMARYS = register("battle.elite.amarys");
    public static final SoundEvent ELITE_LACEY = register("battle.elite.lacey");
    public static final SoundEvent ELITE_DRAYTON = register("battle.elite.drayton");
    public static final SoundEvent CHAMPION_KIERAN = register("battle.champion.kieran");

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
