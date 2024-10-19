package kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor;
import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleactor.EntityBackedTrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.battleactor.DisposableBattlePokemonFactory;
import kiwiapollo.cobblemontrainerbattle.common.BattleCondition;
import kiwiapollo.cobblemontrainerbattle.common.Generation5AI;
import kiwiapollo.cobblemontrainerbattle.entities.TrainerEntity;
import kiwiapollo.cobblemontrainerbattle.exception.PokemonParseException;
import kiwiapollo.cobblemontrainerbattle.parser.SmogonPokemon;
import kiwiapollo.cobblemontrainerbattle.parser.SmogonPokemonParser;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class EntityBackedTrainer implements TrainerBattleParticipant {
    private final TrainerBattleParticipant trainer;
    private final TrainerEntity entity;

    public EntityBackedTrainer(Identifier identifier, TrainerEntity entity, ServerPlayerEntity player) {
        this.trainer = new NormalBattleTrainer(identifier, player);
        this.entity = entity;
    }

    @Override
    public String getName() {
        return trainer.getName();
    }

    @Override
    public UUID getUuid() {
        return trainer.getUuid();
    }

    @Override
    public Identifier getIdentifier() {
        return trainer.getIdentifier();
    }

    @Override
    public BattleAI getBattleAI() {
        return trainer.getBattleAI();
    }

    @Override
    public BattleCondition getBattleCondition() {
        return trainer.getBattleCondition();
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
        entity.setAiDisabled(false);
    }

    @Override
    public void onDefeat() {
        entity.discard();
    }

    @Override
    public PartyStore getParty() {
        return trainer.getParty();
    }

    @Override
    public void setParty(PartyStore party) {
        trainer.setParty(party);
    }

    @Override
    public List<BattlePokemon> getBattleTeam() {
        return trainer.getBattleTeam();
    }
}
