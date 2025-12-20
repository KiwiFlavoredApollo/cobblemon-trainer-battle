package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class KalosSoundEvent {
    public static final List<SoundEvent> all = new ArrayList<>();

    public static final SoundEvent KALOS_DEFAULT = register("battle.kalos.default");
    public static final SoundEvent LEADER_VIOLA = register("battle.leader.viola");
    public static final SoundEvent LEADER_GRANT = register("battle.leader.grant");
    public static final SoundEvent LEADER_KORRINA = register("battle.leader.korrina");
    public static final SoundEvent LEADER_RAMOS = register("battle.leader.ramos");
    public static final SoundEvent LEADER_CLEMONT = register("battle.leader.clemont");
    public static final SoundEvent LEADER_VALERIE = register("battle.leader.valerie");
    public static final SoundEvent LEADER_OLYMPIA = register("battle.leader.olympia");
    public static final SoundEvent LEADER_WULFRIC = register("battle.leader.wulfric");
    public static final SoundEvent ELITE_MALVA = register("battle.elite.malva");
    public static final SoundEvent ELITE_SIEBOLD = register("battle.elite.siebold");
    public static final SoundEvent ELITE_WIKSTROM = register("battle.elite.wikstrom");
    public static final SoundEvent ELITE_DRASNA = register("battle.elite.drasna");
    public static final SoundEvent CHAMPION_DIANTHA = register("battle.champion.diantha");

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
