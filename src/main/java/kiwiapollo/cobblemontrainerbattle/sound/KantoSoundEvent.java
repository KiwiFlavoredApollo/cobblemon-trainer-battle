package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public enum KantoSoundEvent {
    KANTO_DEFAULT("battle.kanto.default"),

    TRAINER_RED("battle.trainer.red"),
    TRAINER_LEAF("battle.trainer.leaf"),

    LEADER_BROCK("battle.leader.brock"),
    LEADER_MISTY("battle.leader.misty"),
    LEADER_LT_SURGE("battle.leader.lt_surge"),
    LEADER_ERIKA("battle.leader.erika"),
    LEADER_KOGA("battle.leader.koga"),
    LEADER_SABRINA("battle.leader.sabrina"),
    LEADER_BLAINE("battle.leader.blaine"),
    LEADER_GIOVANNI("battle.leader.giovanni"),
    ELITE_LORELEI("battle.elite.lorelei"),
    ELITE_BRUNO("battle.elite.bruno"),
    ELITE_AGATHA("battle.elite.agatha"),
    ELITE_LANCE("battle.elite.lance"),
    CHAMPION_TERRY("battle.champion.terry");

    private final Identifier identifier;

    KantoSoundEvent(String path) {
        this.identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, path);
    }

    public SoundEvent getSoundEvent() {
        return SoundEvent.of(identifier);
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
