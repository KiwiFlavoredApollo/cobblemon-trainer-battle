package kiwiapollo.cobblemontrainerbattle.common;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public enum BattleSounds {
    DEFAULT_DISC_1("battle.default.disc_1"),
    DEFAULT_DISC_2("battle.default.disc_2"),
    DEFAULT_DISC_3("battle.default.disc_3"),

    GYM_LEADER_DISC_1("battle.gym_leader.disc_1"),
    GYM_LEADER_DISC_2("battle.gym_leader.disc_2"),
    GYM_LEADER_DISC_3("battle.gym_leader.disc_3"),

    ELITE_FOUR_DISC_1("battle.elite_four.disc_1"),
    ELITE_FOUR_DISC_2("battle.elite_four.disc_2"),
    ELITE_FOUR_DISC_3("battle.elite_four.disc_3"),

    CHAMPION_DISC_1("battle.champion.disc_1"),
    CHAMPION_DISC_2("battle.champion.disc_2"),
    CHAMPION_DISC_3("battle.champion.disc_3");

    private final Identifier identifier;

    BattleSounds(String soundPath) {
        this.identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, soundPath);
    }

    public SoundEvent getSoundEvent() {
        return SoundEvent.of(identifier);
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
