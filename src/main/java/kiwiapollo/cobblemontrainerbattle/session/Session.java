package kiwiapollo.cobblemontrainerbattle.session;

import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.predicates.MessagePredicate;

import java.util.List;

public interface Session {
    void startBattle() throws BattleStartException;

    void onBattleVictory();

    void onBattleDefeat();

    void onSessionStop();

    int getDefeatedTrainersCount();

    List<MessagePredicate<PlayerBattleParticipant>> getBattlePredicates();

    boolean isPlayerDefeated();

    boolean isAllTrainerDefeated();

    boolean isAnyTrainerDefeated();
}
