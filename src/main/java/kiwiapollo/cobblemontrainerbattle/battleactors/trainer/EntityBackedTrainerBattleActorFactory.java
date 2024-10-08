package kiwiapollo.cobblemontrainerbattle.battleactors.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.common.Generation5AI;
import kiwiapollo.cobblemontrainerbattle.battleactors.SafeCopyBattlePokemonFactory;
import kiwiapollo.cobblemontrainerbattle.entities.TrainerEntity;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;

import java.util.List;
import java.util.UUID;

public class EntityBackedTrainerBattleActorFactory implements TrainerBattleActorFactory {
    private final TrainerEntity trainerEntity;

    public EntityBackedTrainerBattleActorFactory(TrainerEntity trainerEntity) {
        this.trainerEntity = trainerEntity;
    }

    @Override
    public BattleActor createWithStatusQuo(Trainer trainer) {
        List<BattlePokemon> pokemons = trainer.pokemons.stream()
                .map(SafeCopyBattlePokemonFactory::create).toList();

        return new EntityBackedTrainerBattleActor(
                trainer.name,
                UUID.randomUUID(),
                pokemons,
                new Generation5AI(),
                trainerEntity
        );
    }

    @Override
    public BattleActor createWithFlatLevelFullHealth(Trainer trainer, int level) {
        trainer.pokemons.forEach(Pokemon::heal);
        trainer.pokemons.forEach(pokemon -> pokemon.setLevel(level));

        List<BattlePokemon> pokemons = trainer.pokemons.stream()
                .map(SafeCopyBattlePokemonFactory::create).toList();

        return new EntityBackedTrainerBattleActor(
                trainer.name,
                UUID.randomUUID(),
                pokemons,
                new Generation5AI(),
                trainerEntity
        );
    }
}
