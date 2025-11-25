package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public enum JohtoSoundEvent {
    JOHTO_DEFAULT("battle.johto.default"),

    TRAINER_EUSINE("battle.trainer.eusine"),
    TRAINER_SILVER("battle.trainer.silver"),

    LEADER_FALKNER("battle.leader.falkner"),
    LEADER_BUGSY("battle.leader.bugsy"),
    LEADER_WHITNEY("battle.leader.whitney"),
    LEADER_MORTY("battle.leader.morty"),
    LEADER_CHUCK("battle.leader.chuck"),
    LEADER_JASMINE("battle.leader.jasmine"),
    LEADER_PRYCE("battle.leader.pryce"),
    LEADER_CLAIR("battle.leader.clair"),
    ELITE_WILL("battle.elite.will"),
    ELITE_KOGA("battle.elite.koga"),
    ELITE_BRUNO("battle.elite.bruno"),
    ELITE_KAREN("battle.elite.karen"),
    CHAMPION_LANCE("battle.champion.lance");

    private final Identifier identifier;

    JohtoSoundEvent(String path) {
        this.identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, path);
    }

    public SoundEvent getSoundEvent() {
        return SoundEvent.of(identifier);
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
