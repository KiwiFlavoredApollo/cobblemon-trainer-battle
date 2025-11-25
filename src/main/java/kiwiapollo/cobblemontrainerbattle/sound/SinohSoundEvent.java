package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public enum SinohSoundEvent {
    SINNOH_DEFAULT("battle.sinnoh.default"),

    LEADER_ROARK("battle.leader.roark"),
    LEADER_GARDENIA("battle.leader.gardenia"),
    LEADER_MAYLENE("battle.leader.maylene"),
    LEADER_CRASHER_WAKE("battle.leader.crasher_wake"),
    LEADER_FANTINA("battle.leader.fantina"),
    LEADER_BYRON("battle.leader.byron"),
    LEADER_CANDICE("battle.leader.candice"),
    LEADER_VOLKNER("battle.leader.volkner"),
    ELITE_AARON("battle.elite.aaron"),
    ELITE_BERTHA("battle.elite.bertha"),
    ELITE_FLINT("battle.elite.flint"),
    ELITE_LUCIAN("battle.elite.lucian"),
    CHAMPION_CYNTHIA("battle.champion.cynthia");

    private final Identifier identifier;

    SinohSoundEvent(String path) {
        this.identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, path);
    }

    public SoundEvent getSoundEvent() {
        return SoundEvent.of(identifier);
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
