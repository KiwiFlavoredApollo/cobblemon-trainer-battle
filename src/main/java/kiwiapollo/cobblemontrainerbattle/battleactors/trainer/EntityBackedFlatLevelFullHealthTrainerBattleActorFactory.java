package kiwiapollo.cobblemontrainerbattle.battleactors.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.common.Generation5AI;
import kiwiapollo.cobblemontrainerbattle.npc.TrainerEntity;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;
import kotlin.Unit;

import java.util.UUID;

public class EntityBackedFlatLevelFullHealthTrainerBattleActorFactory {
    public BattleActor create(Trainer trainer, int level, TrainerEntity trainerEntity) {
        trainer.pokemons.forEach(Pokemon::heal);
        trainer.pokemons.forEach(pokemon -> pokemon.setLevel(level));
        return new EntityBackedTrainerBattleActorFactory().create(trainer, trainerEntity);
    }
}
