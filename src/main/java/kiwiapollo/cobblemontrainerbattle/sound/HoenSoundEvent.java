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

    public static final SoundEvent HOENN_DEFAULT = register("battle.hoenn.default");
    public static final SoundEvent LEADER_ROXANNE = register("battle.leader.roxanne");
    public static final SoundEvent LEADER_BRAWLY = register("battle.leader.brawly");
    public static final SoundEvent LEADER_WATTSON = register("battle.leader.wattson");
    public static final SoundEvent LEADER_FLANNERY = register("battle.leader.flannery");
    public static final SoundEvent LEADER_NORMAN = register("battle.leader.norman");
    public static final SoundEvent LEADER_WINONA = register("battle.leader.winona");
    public static final SoundEvent LEADER_TATE_AND_LIZA = register("battle.leader.tate_and_liza");
    public static final SoundEvent LEADER_JUAN = register("battle.leader.juan");
    public static final SoundEvent ELITE_SIDNEY = register("battle.elite.sidney");
    public static final SoundEvent ELITE_PHOEBE = register("battle.elite.phoebe");
    public static final SoundEvent ELITE_GLACIA = register("battle.elite.glacia");
    public static final SoundEvent ELITE_DRAKE = register("battle.elite.drake");
    public static final SoundEvent CHAMPION_WALLACE = register("battle.champion.wallace");

    public static void initialize() {

    }

    private static SoundEvent register(String name) {
        try {
            Identifier identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, name);
            SoundEvent registered = Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
            all.add(registered);

            return registered;
            
        } catch (RuntimeException e) {
            Identifier identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, name);
            SoundEvent sound = SoundEvent.of(identifier);
            all.add(sound);

            return sound;
        }
    }

    public static List<SoundEvent> getAll() {
        return new ArrayList<>(all);
    }
}
