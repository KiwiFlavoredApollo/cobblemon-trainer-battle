package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.factory;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.session.Session;
import kiwiapollo.cobblemontrainerbattle.exception.AllTrainerDefeatedException;

public interface SessionTrainerFactory {
    TrainerBattleParticipant create(Session session) throws AllTrainerDefeatedException;
}
