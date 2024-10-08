package kiwiapollo.cobblemontrainerbattle.battleactors.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;
import net.minecraft.server.network.ServerPlayerEntity;

public class FlatLevelFullHealthTrainerBattleActorFactory {
    public BattleActor create(Trainer trainer, ServerPlayerEntity player, int level) {
        trainer.pokemons.forEach(Pokemon::heal);
        trainer.pokemons.forEach(pokemon -> pokemon.setLevel(level));
        return new TrainerBattleActorFactory().create(trainer, player);
    }
}
