package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public enum KalosSoundEvent {
    KALOS_DEFAULT("battle.kalos.default"),

    LEADER_VIOLA("battle.leader.viola"),
    LEADER_GRANT("battle.leader.grant"),
    LEADER_KORRINA("battle.leader.korrina"),
    LEADER_RAMOS("battle.leader.ramos"),
    LEADER_CLEMONT("battle.leader.clemont"),
    LEADER_VALERIE("battle.leader.valerie"),
    LEADER_OLYMPIA("battle.leader.olympia"),
    LEADER_WULFRIC("battle.leader.wulfric"),
    ELITE_MALVA("battle.elite.malva"),
    ELITE_SIEBOLD("battle.elite.siebold"),
    ELITE_WIKSTROM("battle.elite.wikstrom"),
    ELITE_DRASNA("battle.elite.drasna"),
    CHAMPION_DIANTHA("battle.champion.diantha");

    private final Identifier identifier;

    KalosSoundEvent(String path) {
        this.identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, path);
    }

    public SoundEvent getSoundEvent() {
        return SoundEvent.of(identifier);
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
