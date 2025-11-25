package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public enum AlolaSoundEvent {
    ALOLA_DEFAULT("battle.alola.default"),

    LEADER_ILIMA("battle.leader.ilima"),
    LEADER_LANA("battle.leader.lana"),
    LEADER_KIAWE("battle.leader.kiawe"),
    LEADER_MALLOW("battle.leader.mallow"),
    LEADER_SOPHOCLES("battle.leader.sophocles"),
    LEADER_MINA("battle.leader.mina"),
    LEADER_ACEROLA("battle.leader.acerola"),
    ELITE_HALA("battle.elite.hala"),
    ELITE_ACEROLA("battle.elite.acerola"),
    ELITE_KAHILI("battle.elite.kahili"),
    ELITE_MOLAYNE("battle.elite.molayne"),
    CHAMPION_KUKUI("battle.champion.kukui");

    private final Identifier identifier;

    AlolaSoundEvent(String path) {
        this.identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, path);
    }

    public SoundEvent getSoundEvent() {
        return SoundEvent.of(identifier);
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
