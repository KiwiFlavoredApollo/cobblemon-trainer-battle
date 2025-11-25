package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public enum PaldeaSoundEvent {
    PALDEA_DEFAULT("battle.paldea.default"),

    LEADER_KATY("battle.leader.katy"),
    LEADER_BRASSIUS("battle.leader.brassius"),
    LEADER_IONO("battle.leader.iono"),
    LEADER_KOFU("battle.leader.kofu"),
    LEADER_LARRY("battle.leader.larry"),
    LEADER_RYME("battle.leader.ryme"),
    LEADER_TULIP("battle.leader.tulip"),
    LEADER_GRUSHA("battle.leader.grusha"),
    ELITE_RIKA("battle.elite.rika"),
    ELITE_POPPY("battle.elite.poppy"),
    ELITE_LARRY("battle.elite.larry"),
    ELITE_HASSEL("battle.elite.hassel"),
    CHAMPION_GEETA("battle.champion.geeta"),

    ELITE_CRISPIN("battle.elite.crispin"),
    ELITE_AMARYS("battle.elite.amarys"),
    ELITE_LACEY("battle.elite.lacey"),
    ELITE_DRAYTON("battle.elite.drayton"),
    CHAMPION_KIERAN("battle.champion.kieran");

    private final Identifier identifier;

    PaldeaSoundEvent(String path) {
        this.identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, path);
    }

    public SoundEvent getSoundEvent() {
        return SoundEvent.of(identifier);
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
