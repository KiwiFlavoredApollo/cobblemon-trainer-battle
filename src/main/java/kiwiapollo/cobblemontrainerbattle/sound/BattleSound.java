package kiwiapollo.cobblemontrainerbattle.sound;

import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public enum BattleSound {
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

    // Kanto
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
    CHAMPION_TERRY("battle.champion.terry"),
    TRAINER_RED("battle.trainer.red"),
    TRAINER_LEAF("battle.trainer.leaf"),

    // Johto
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
    // ELITE_BRUNO("battle.elite.bruno"),
    ELITE_KAREN("battle.elite.karen"),
    CHAMPION_LANCE("battle.champion.lance"),
    TRAINER_EUSINE("battle.trainer.eusine"),
    TRAINER_SILVER("battle.trainer.silver"),

    // Hoenn
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

    // Sinnoh
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
    CHAMPION_CYNTHIA("battle.champion.cynthia"),

    // Unova
    LEADER_CILAN("battle.leader.cilan"),
    LEADER_CHILI("battle.leader.chili"),
    LEADER_CRESS("battle.leader.cress"),
    LEADER_LENORA("battle.leader.lenora"),
    LEADER_BURGH("battle.leader.burgh"),
    LEADER_ELESA("battle.leader.elesa"),
    LEADER_CLAY("battle.leader.clay"),
    LEADER_SKYLA("battle.leader.skyla"),
    LEADER_BRYCEN("battle.leader.brycen"),
    LEADER_IRIS("battle.leader.iris"),
    ELITE_SHAUNTAL("battle.elite.shauntal"),
    ELITE_MARSHAL("battle.elite.marshal"),
    ELITE_GRIMSLEY("battle.elite.grimsley"),
    ELITE_CAITLIN("battle.elite.caitlin"),
    CHAMPION_ALDER("battle.champion.alder"),

    LEADER_CHEREN("battle.leader.cheren"),
    LEADER_ROXIE("battle.leader.roxie"),
    // LEADER_BURGH("battle.leader.burgh"),
    // LEADER_ELESA("battle.leader.elesa"),
    // LEADER_CLAY("battle.leader.clay"),
    // LEADER_SKYLA("battle.leader.skyla"),
    LEADER_DRAYDEN("battle.leader.drayden"),
    LEADER_MARLON("battle.leader.marlon"),
    // ELITE_SHAUNTAL("battle.elite.shauntal"),
    // ELITE_MARSHAL("battle.elite.marshal"),
    // ELITE_GRIMSLEY("battle.elite.grimsley"),
    // ELITE_CAITLIN("battle.elite.caitlin"),
    CHAMPION_IRIS("battle.champion.iris"),

    // Kalos
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
    CHAMPION_DIANTHA("battle.champion.diantha"),

    // Alola
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
    CHAMPION_KUKUI("battle.champion.kukui"),

    // Galar
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
    CHAMPION_LEON("battle.champion.leon"),

    // Paldea
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

    // The Indigo Disk
    ELITE_CRISPIN("battle.elite.crispin"),
    ELITE_AMARYS("battle.elite.amarys"),
    ELITE_LACEY("battle.elite.lacey"),
    ELITE_DRAYTON("battle.elite.drayton"),
    CHAMPION_KIERAN("battle.champion.kieran");

    private final Identifier identifier;

    BattleSound(String path) {
        this.identifier = Identifier.of(CobblemonTrainerBattle.MOD_ID, path);
    }

    public SoundEvent getSoundEvent() {
        return SoundEvent.of(identifier);
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
