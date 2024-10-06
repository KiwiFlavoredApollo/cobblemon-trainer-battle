package kiwiapollo.cobblemontrainerbattle.battleactors.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.battles.actor.TrainerBattleActor;
import com.cobblemon.mod.common.battles.ai.RandomBattleAI;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import kiwiapollo.cobblemontrainerbattle.common.Generation5AI;
import kiwiapollo.cobblemontrainerbattle.common.SafeCopyBattlePokemonFactory;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;
import kotlin.Unit;

import java.util.UUID;

public class TrainerBattleActorFactory {
    public BattleActor create(Trainer trainer) {
        return new TrainerBattleActor(
                trainer.name,
                UUID.randomUUID(),
                trainer.pokemons.stream().map(SafeCopyBattlePokemonFactory::create).toList(),
                new Generation5AI()
        );
    }
}
