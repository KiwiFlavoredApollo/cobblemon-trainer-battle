package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.EntityBackedTrainer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntity;

public class EntityBackedTrainerBattle extends AbstractTrainerBattle {
    public EntityBackedTrainerBattle(PlayerBattleParticipant player, TrainerBattleParticipant trainer, TrainerEntity entity) {
        super(player, new EntityBackedTrainer(trainer, entity));
    }
}
