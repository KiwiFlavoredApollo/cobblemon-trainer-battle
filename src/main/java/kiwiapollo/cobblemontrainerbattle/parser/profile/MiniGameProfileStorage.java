package kiwiapollo.cobblemontrainerbattle.parser.profile;

import kiwiapollo.cobblemontrainerbattle.battle.battlefactory.BattleFactoryProfile;

public class MiniGameProfileStorage {
    private static BattleFactoryProfile battleFactory;

    public static BattleFactoryProfile getBattleFactoryProfile() {
        return battleFactory;
    }

    public static void setBattleFactoryProfile(BattleFactoryProfile battleFactoryProfile) {
        MiniGameProfileStorage.battleFactory = battleFactoryProfile;
    }
}
