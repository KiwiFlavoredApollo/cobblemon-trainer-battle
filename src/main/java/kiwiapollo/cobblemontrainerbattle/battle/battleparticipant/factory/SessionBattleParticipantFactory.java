package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.factory;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.session.Session;

public interface SessionBattleParticipantFactory {
    PlayerBattleParticipant createPlayer(Session session);

    TrainerBattleParticipant createTrainer(Session session);
}
