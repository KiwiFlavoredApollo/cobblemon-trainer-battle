package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public enum UnovaWhiteSoundEvent {
    UNOVA_DEFAULT("battle.unova.default"),

    LEADER_CHEREN("battle.leader.cheren"),
    LEADER_ROXIE("battle.leader.roxie"),
    LEADER_BURGH("battle.leader.burgh"),
    LEADER_ELESA("battle.leader.elesa"),
    LEADER_CLAY("battle.leader.clay"),
    LEADER_SKYLA("battle.leader.skyla"),
    LEADER_DRAYDEN("battle.leader.drayden"),
    LEADER_MARLON("battle.leader.marlon"),
    ELITE_SHAUNTAL("battle.elite.shauntal"),
    ELITE_MARSHAL("battle.elite.marshal"),
    ELITE_GRIMSLEY("battle.elite.grimsley"),
    ELITE_CAITLIN("battle.elite.caitlin"),
    CHAMPION_IRIS("battle.champion.iris");

    private final Identifier identifier;

    UnovaWhiteSoundEvent(String path) {
        this.identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, path);
    }

    public SoundEvent getSoundEvent() {
        return SoundEvent.of(identifier);
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
