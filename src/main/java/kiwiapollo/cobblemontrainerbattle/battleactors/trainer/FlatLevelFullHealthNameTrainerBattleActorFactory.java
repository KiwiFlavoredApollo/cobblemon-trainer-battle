package kiwiapollo.cobblemontrainerbattle.battleactors.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;

public class FlatLevelFullHealthNameTrainerBattleActorFactory {
    public BattleActor create(Trainer trainer, int level) {
        trainer.pokemons.forEach(Pokemon::heal);
        trainer.pokemons.forEach(pokemon -> pokemon.setLevel(level));
        return new TrainerBattleActorFactory().create(trainer);
    }
}
