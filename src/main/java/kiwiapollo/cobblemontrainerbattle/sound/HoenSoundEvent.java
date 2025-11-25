package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public enum HoenSoundEvent {
    HOENN_DEFAULT("battle.hoenn.default"),

    LEADER_ROXANNE("battle.leader.roxanne"),
    LEADER_BRAWLY("battle.leader.brawly"),
    LEADER_WATTSON("battle.leader.wattson"),
    LEADER_FLANNERY("battle.leader.flannery"),
    LEADER_NORMAN("battle.leader.norman"),
    LEADER_WINONA("battle.leader.winona"),
    LEADER_TATE_AND_LIZA("battle.leader.tate_and_liza"),
    LEADER_JUAN("battle.leader.juan"),
    ELITE_SIDNEY("battle.elite.sidney"),
    ELITE_PHOEBE("battle.elite.phoebe"),
    ELITE_GLACIA("battle.elite.glacia"),
    ELITE_DRAKE("battle.elite.drake"),
    CHAMPION_WALLACE("battle.champion.wallace");

    private final Identifier identifier;

    HoenSoundEvent(String path) {
        this.identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, path);
    }

    public SoundEvent getSoundEvent() {
        return SoundEvent.of(identifier);
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
