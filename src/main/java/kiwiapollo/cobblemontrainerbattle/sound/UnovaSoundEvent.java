package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class UnovaSoundEvent {
    public static void initialize() {
        DiscOne.initialize();
        DiscTwo.initialize();
    }

    private static class DiscOne {
        public static final List<SoundEvent> all = new ArrayList<>();

        public static final SoundEvent UNOVA_DEFAULT = registerOrIgnore("battle.unova.default");
        public static final SoundEvent LEADER_CILAN = registerOrIgnore("battle.leader.cilan");
        public static final SoundEvent LEADER_CHILI = registerOrIgnore("battle.leader.chili");
        public static final SoundEvent LEADER_CRESS = registerOrIgnore("battle.leader.cress");
        public static final SoundEvent LEADER_LENORA = registerOrIgnore("battle.leader.lenora");
        public static final SoundEvent LEADER_BURGH = registerOrIgnore("battle.leader.burgh");
        public static final SoundEvent LEADER_ELESA = registerOrIgnore("battle.leader.elesa");
        public static final SoundEvent LEADER_CLAY = registerOrIgnore("battle.leader.clay");
        public static final SoundEvent LEADER_SKYLA = registerOrIgnore("battle.leader.skyla");
        public static final SoundEvent LEADER_BRYCEN = registerOrIgnore("battle.leader.brycen");
        public static final SoundEvent LEADER_DRAYDEN = registerOrIgnore("battle.leader.drayden");
        public static final SoundEvent LEADER_IRIS = registerOrIgnore("battle.leader.iris");
        public static final SoundEvent ELITE_SHAUNTAL = registerOrIgnore("battle.elite.shauntal");
        public static final SoundEvent ELITE_MARSHAL = registerOrIgnore("battle.elite.marshal");
        public static final SoundEvent ELITE_GRIMSLEY = registerOrIgnore("battle.elite.grimsley");
        public static final SoundEvent ELITE_CAITLIN = registerOrIgnore("battle.elite.caitlin");
        public static final SoundEvent CHAMPION_ALDER = registerOrIgnore("battle.champion.alder");

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

    private static class DiscTwo {
        public static final List<SoundEvent> all = new ArrayList<>();

        public static final SoundEvent UNOVA_DEFAULT = registerOrIgnore("battle.unova.default");
        public static final SoundEvent LEADER_CHEREN = registerOrIgnore("battle.leader.cheren");
        public static final SoundEvent LEADER_ROXIE = registerOrIgnore("battle.leader.roxie");
        public static final SoundEvent LEADER_BURGH = registerOrIgnore("battle.leader.burgh");
        public static final SoundEvent LEADER_ELESA = registerOrIgnore("battle.leader.elesa");
        public static final SoundEvent LEADER_CLAY = registerOrIgnore("battle.leader.clay");
        public static final SoundEvent LEADER_SKYLA = registerOrIgnore("battle.leader.skyla");
        public static final SoundEvent LEADER_DRAYDEN = registerOrIgnore("battle.leader.drayden");
        public static final SoundEvent LEADER_MARLON = registerOrIgnore("battle.leader.marlon");
        public static final SoundEvent ELITE_SHAUNTAL = registerOrIgnore("battle.elite.shauntal");
        public static final SoundEvent ELITE_MARSHAL = registerOrIgnore("battle.elite.marshal");
        public static final SoundEvent ELITE_GRIMSLEY = registerOrIgnore("battle.elite.grimsley");
        public static final SoundEvent ELITE_CAITLIN = registerOrIgnore("battle.elite.caitlin");
        public static final SoundEvent CHAMPION_IRIS = registerOrIgnore("battle.champion.iris");

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
}
