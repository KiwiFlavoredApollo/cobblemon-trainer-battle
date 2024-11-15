package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.EntityBackedTrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class EntityBackedTrainer extends NormalBattleTrainer {
    private final TrainerEntity entity;

    public EntityBackedTrainer(Identifier identifier, TrainerEntity entity, ServerPlayerEntity player) {
        super(identifier, player);
        this.entity = entity;
    }

    @Override
    public AIBattleActor createBattleActor() {
        return new EntityBackedTrainerBattleActor(
                getName(),
                getUuid(),
                getBattleTeam(),
                getBattleAI(),
                entity
        );
    }

    @Override
    public void onVictory() {
        entity.onVictory();
        super.onVictory();
    }

    @Override
    public void onDefeat() {
        entity.onDefeat();
        super.onDefeat();
    }
}
