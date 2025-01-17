package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer;

import kiwiapollo.cobblemontrainerbattle.common.SimpleFactory;
import kiwiapollo.cobblemontrainerbattle.global.preset.TrainerStorage;
import net.minecraft.entity.LivingEntity;

public class EntityBackedTrainerBattleParticipantFactory implements SimpleFactory<TrainerBattleParticipant> {
    private final String trainer;
    private final LivingEntity entity;

    public EntityBackedTrainerBattleParticipantFactory(String trainer, LivingEntity entity) {
        this.trainer = trainer;
        this.entity = entity;
    }

    @Override
    public TrainerBattleParticipant create() {
        return new EntityBackedTrainer(TrainerStorage.getInstance().get(trainer), entity);
    }
}
