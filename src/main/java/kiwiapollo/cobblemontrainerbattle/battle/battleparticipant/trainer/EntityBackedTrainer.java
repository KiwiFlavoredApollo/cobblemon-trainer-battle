package kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor;
import com.cobblemon.mod.common.api.battles.model.ai.BattleAI;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import kiwiapollo.cobblemontrainerbattle.battle.battleactor.EntityBackedTrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.MessagePredicate;
import kiwiapollo.cobblemontrainerbattle.common.LevelMode;
import kiwiapollo.cobblemontrainerbattle.entity.TrainerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EntityBackedTrainer implements TrainerBattleParticipant {
    private final TrainerBattleParticipant trainer;
    private final TrainerEntity entity;

    public EntityBackedTrainer(TrainerBattleParticipant trainer, TrainerEntity entity) {
        this.trainer = trainer;
        this.entity = entity;
    }

    @Override
    public String getId() {
        return trainer.getId();
    }

    @Override
    public BattleAI getBattleAI() {
        return trainer.getBattleAI();
    }

    @Override
    public BattleFormat getBattleFormat() {
        return trainer.getBattleFormat();
    }

    @Override
    public Optional<SoundEvent> getBattleTheme() {
        return trainer.getBattleTheme();
    }

    @Override
    public LevelMode getLevelMode() {
        return trainer.getLevelMode();
    }

    @Override
    public boolean isSpawningAllowed() {
        return trainer.isSpawningAllowed();
    }

    @Override
    public List<MessagePredicate<PlayerBattleParticipant>> getPredicates() {
        return trainer.getPredicates();
    }

    @Override
    public AIBattleActor createBattleActor(ServerPlayerEntity player) {
        return new EntityBackedTrainerBattleActor(
                getName(),
                getUuid(),
                getBattleTeam(),
                getBattleAI(),
                entity
        );
    }

    @Override
    public void onPlayerDefeat(ServerPlayerEntity player) {
        entity.onPlayerDefeat();
        trainer.onPlayerDefeat(player);
    }

    @Override
    public void onPlayerVictory(ServerPlayerEntity player) {
        entity.onPlayerVictory();
        trainer.onPlayerVictory(player);
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
    public PartyStore getParty() {
        return trainer.getParty();
    }

    @Override
    public List<BattlePokemon> getBattleTeam() {
        return trainer.getBattleTeam();
    }
}
