package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public enum UnovaBlackSoundEvent {
    UNOVA_DEFAULT("battle.unova.default"),

    LEADER_CILAN("battle.leader.cilan"),
    LEADER_CHILI("battle.leader.chili"),
    LEADER_CRESS("battle.leader.cress"),
    LEADER_LENORA("battle.leader.lenora"),
    LEADER_BURGH("battle.leader.burgh"),
    LEADER_ELESA("battle.leader.elesa"),
    LEADER_CLAY("battle.leader.clay"),
    LEADER_SKYLA("battle.leader.skyla"),
    LEADER_BRYCEN("battle.leader.brycen"),
    LEADER_DRAYDEN("battle.leader.drayden"),
    LEADER_IRIS("battle.leader.iris"),
    ELITE_SHAUNTAL("battle.elite.shauntal"),
    ELITE_MARSHAL("battle.elite.marshal"),
    ELITE_GRIMSLEY("battle.elite.grimsley"),
    ELITE_CAITLIN("battle.elite.caitlin"),
    CHAMPION_ALDER("battle.champion.alder");

    private final Identifier identifier;

    UnovaBlackSoundEvent(String path) {
        this.identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, path);
    }

    public SoundEvent getSoundEvent() {
        return SoundEvent.of(identifier);
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
