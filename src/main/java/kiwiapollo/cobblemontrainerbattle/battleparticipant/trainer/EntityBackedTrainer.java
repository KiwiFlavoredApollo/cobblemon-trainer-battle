package kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor;
import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import kiwiapollo.cobblemontrainerbattle.battleactor.EntityBackedTrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.common.BattleCondition;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntity;
import kiwiapollo.cobblemontrainerbattle.parser.profile.TrainerProfileStorage;
import kiwiapollo.cobblemontrainerbattle.postbattle.DefeatActionSetHandler;
import kiwiapollo.cobblemontrainerbattle.postbattle.VictoryActionSetHandler;
import kiwiapollo.cobblemontrainerbattle.predicates.MaximumPartyLevelPredicate;
import kiwiapollo.cobblemontrainerbattle.predicates.MessagePredicate;
import kiwiapollo.cobblemontrainerbattle.predicates.MinimumPartyLevelPredicate;
import kiwiapollo.cobblemontrainerbattle.predicates.RematchAllowedPredicate;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerProfile;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.UUID;

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
