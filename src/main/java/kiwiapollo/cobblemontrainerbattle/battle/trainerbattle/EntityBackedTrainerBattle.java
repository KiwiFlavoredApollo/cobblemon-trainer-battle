package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle;

import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.EntityBackedTrainer;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipant;
import net.minecraft.entity.LivingEntity;

public class EntityBackedTrainerBattle extends AbstractTrainerBattle {
    public EntityBackedTrainerBattle(PlayerBattleParticipant player, TrainerBattleParticipant trainer, LivingEntity entity) {
        super(player, new EntityBackedTrainer(trainer, entity));
    }
}
