package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public enum BattleSounds {
    LEADER_DEFAULT("battle.leader.default"),
    ELITE_DEFAULT("battle.elite.default"),
    CHAMPION_DEFAULT("battle.champion.default"),

    KANTO_DEFAULT("battle.kanto.default"),
    JOHTO_DEFAULT("battle.johto.default"),
    HOENN_DEFAULT("battle.hoenn.default"),
    SINNOH_DEFAULT("battle.sinnoh.default"),
    UNOVA_DEFAULT("battle.unova.default"),
    KALOS_DEFAULT("battle.kalos.default"),
    ALOLA_DEFAULT("battle.alola.default"),
    GALAR_DEFAULT("battle.galar.default"),
    HISUI_DEFAULT("battle.hisui.default"),
    PALDEA_DEFAULT("battle.paldea.default"),

    LEADER_BROCK("battle.leader.brock"),
    LEADER_MISTY("battle.leader.misty"),
    LEADER_LT_SURGE("battle.leader.lt_surge"),
    LEADER_ERIKA("battle.leader.erika"),
    LEADER_KOGA("battle.leader.koga"),
    LEADER_SABRINA("battle.leader.sabrina"),
    LEADER_BLAINE("battle.leader.blaine"),
    LEADER_GIOVANNI("battle.leader.giovanni"),

    LEADER_FALKNER("battle.leader.falkner"),
    LEADER_BUGSY("battle.leader.bugsy"),
    LEADER_WHITNEY("battle.leader.whitney"),
    LEADER_MORTY("battle.leader.morty"),
    LEADER_CHUCK("battle.leader.chuck"),
    LEADER_JASMINE("battle.leader.jasmine"),
    LEADER_PRYCE("battle.leader.pryce"),
    LEADER_CLAIR("battle.leader.clair"),

    ELITE_LORELEI("battle.elite.lorelei"),
    ELITE_BRUNO("battle.elite.bruno"),
    ELITE_AGATHA("battle.elite.agatha"),
    ELITE_LANCE("battle.elite.lance"),

    CHAMPION_TERRY("battle.champion.terry"),

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

    CHAMPION_WALLACE("battle.champion.wallace"),

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

    BattleSounds(String path) {
        this.identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, path);
    }

    public SoundEvent getSoundEvent() {
        return SoundEvent.of(identifier);
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
