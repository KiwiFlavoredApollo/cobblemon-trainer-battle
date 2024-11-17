package kiwiapollo.cobblemontrainerbattle.battle.session;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.MessagePredicate;
import net.minecraft.sound.SoundEvent;

import java.util.List;
import java.util.Optional;

public interface Session {
    void startBattle() throws BattleStartException;

    void onBattleVictory();

    void onBattleDefeat();

    void onSessionStop();

    int getDefeatedTrainersCount(); // TODO rename it getRoundCount()

    List<MessagePredicate<PlayerBattleParticipant>> getBattlePredicates();

    boolean isPlayerDefeated();

    boolean isAllTrainerDefeated();

    boolean isAnyTrainerDefeated();

    Optional<SoundEvent> getBattleTheme();
}
