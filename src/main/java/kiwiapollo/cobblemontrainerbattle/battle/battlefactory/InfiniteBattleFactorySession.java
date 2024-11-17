package kiwiapollo.cobblemontrainerbattle.battle.battlefactory;

import kiwiapollo.cobblemontrainerbattle.advancement.CustomCriteria;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.factory.InfiniteBattleFactoryParticipantFactory;
import kiwiapollo.cobblemontrainerbattle.parser.history.MaximumStreakRecord;
import kiwiapollo.cobblemontrainerbattle.parser.history.PlayerHistoryManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class InfiniteBattleFactorySession extends BattleFactorySession {
    public InfiniteBattleFactorySession(ServerPlayerEntity player) {
        super(new InfiniteBattleFactoryParticipantFactory(player));
    }

    @Override
    public void onSessionStop() {
        updateStreakRecord();
        CustomCriteria.BATTLE_FACTORY_WINNING_STREAK_CRITERION.trigger(player.getPlayerEntity());
    }

    private MaximumStreakRecord getBattleRecord() {
        return (MaximumStreakRecord) PlayerHistoryManager.get(player.getUuid()).get(Identifier.tryParse("minigame:battlefactory"));
    }

    private void updateStreakRecord() {
        int oldStreak = getBattleRecord().getMaximumStreak();
        int newStreak = getStreak();

        if (newStreak > oldStreak) {
            getBattleRecord().setMaximumStreak(newStreak);
        }
    }
}
