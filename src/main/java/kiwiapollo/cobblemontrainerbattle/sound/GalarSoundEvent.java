package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public enum GalarSoundEvent {
    GALAR_DEFAULT("battle.galar.default"),

    LEADER_MILO("battle.leader.milo"),
    LEADER_NESSA("battle.leader.nessa"),
    LEADER_KABU("battle.leader.kabu"),
    LEADER_BEA("battle.leader.bea"),
    LEADER_ALLISTER("battle.leader.allister"),
    LEADER_OPAL("battle.leader.opal"),
    LEADER_GORDIE("battle.leader.gordie"),
    LEADER_MELONY("battle.leader.melony"),
    LEADER_PIERS("battle.leader.piers"),
    LEADER_RAIHAN("battle.leader.raihan"),
    CHAMPION_LEON("battle.champion.leon");

    private final Identifier identifier;

    GalarSoundEvent(String path) {
        this.identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, path);
    }

    public SoundEvent getSoundEvent() {
        return SoundEvent.of(identifier);
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
