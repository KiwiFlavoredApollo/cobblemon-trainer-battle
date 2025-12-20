package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class KantoSoundEvent {
    public static final List<SoundEvent> all = new ArrayList<>();

    public static final SoundEvent KANTO_DEFAULT = register("battle.kanto.default");
    public static final SoundEvent TRAINER_RED = register("battle.trainer.red");
    public static final SoundEvent TRAINER_LEAF = register("battle.trainer.leaf");
    public static final SoundEvent LEADER_BROCK = register("battle.leader.brock");
    public static final SoundEvent LEADER_MISTY = register("battle.leader.misty");
    public static final SoundEvent LEADER_LT_SURGE = register("battle.leader.lt_surge");
    public static final SoundEvent LEADER_ERIKA = register("battle.leader.erika");
    public static final SoundEvent LEADER_KOGA = register("battle.leader.koga");
    public static final SoundEvent LEADER_SABRINA = register("battle.leader.sabrina");
    public static final SoundEvent LEADER_BLAINE = register("battle.leader.blaine");
    public static final SoundEvent LEADER_GIOVANNI = register("battle.leader.giovanni");
    public static final SoundEvent ELITE_LORELEI = register("battle.elite.lorelei");
    public static final SoundEvent ELITE_BRUNO = register("battle.elite.bruno");
    public static final SoundEvent ELITE_AGATHA = register("battle.elite.agatha");
    public static final SoundEvent ELITE_LANCE = register("battle.elite.lance");
    public static final SoundEvent CHAMPION_TERRY = register("battle.champion.terry");

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
