package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class AlolaSoundEvent {
    public static final List<SoundEvent> all = new ArrayList<>();

    public static final SoundEvent ALOLA_DEFAULT = register("battle.alola.default");
    public static final SoundEvent LEADER_ILIMA = register("battle.leader.ilima");
    public static final SoundEvent LEADER_LANA = register("battle.leader.lana");
    public static final SoundEvent LEADER_KIAWE = register("battle.leader.kiawe");
    public static final SoundEvent LEADER_MALLOW = register("battle.leader.mallow");
    public static final SoundEvent LEADER_SOPHOCLES = register("battle.leader.sophocles");
    public static final SoundEvent LEADER_MINA = register("battle.leader.mina");
    public static final SoundEvent LEADER_ACEROLA = register("battle.leader.acerola");
    public static final SoundEvent ELITE_HALA = register("battle.elite.hala");
    public static final SoundEvent ELITE_ACEROLA = register("battle.elite.acerola");
    public static final SoundEvent ELITE_KAHILI = register("battle.elite.kahili");
    public static final SoundEvent ELITE_MOLAYNE = register("battle.elite.molayne");
    public static final SoundEvent CHAMPION_KUKUI = register("battle.champion.kukui");

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
