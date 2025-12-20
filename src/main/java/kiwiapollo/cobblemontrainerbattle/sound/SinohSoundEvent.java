package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class SinohSoundEvent {
    public static final List<SoundEvent> all = new ArrayList<>();

    public static final SoundEvent SINNOH_DEFAULT = register("battle.sinnoh.default");
    public static final SoundEvent LEADER_ROARK = register("battle.leader.roark");
    public static final SoundEvent LEADER_GARDENIA = register("battle.leader.gardenia");
    public static final SoundEvent LEADER_MAYLENE = register("battle.leader.maylene");
    public static final SoundEvent LEADER_CRASHER_WAKE = register("battle.leader.crasher_wake");
    public static final SoundEvent LEADER_FANTINA = register("battle.leader.fantina");
    public static final SoundEvent LEADER_BYRON = register("battle.leader.byron");
    public static final SoundEvent LEADER_CANDICE = register("battle.leader.candice");
    public static final SoundEvent LEADER_VOLKNER = register("battle.leader.volkner");
    public static final SoundEvent ELITE_AARON = register("battle.elite.aaron");
    public static final SoundEvent ELITE_BERTHA = register("battle.elite.bertha");
    public static final SoundEvent ELITE_FLINT = register("battle.elite.flint");
    public static final SoundEvent ELITE_LUCIAN = register("battle.elite.lucian");
    public static final SoundEvent CHAMPION_CYNTHIA = register("battle.champion.cynthia");

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
