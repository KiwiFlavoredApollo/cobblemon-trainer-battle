package kiwiapollo.cobblemontrainerbattle.battleactors.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import kiwiapollo.cobblemontrainerbattle.common.Generation5AI;
import kiwiapollo.cobblemontrainerbattle.common.SafeCopyBattlePokemonFactory;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class TrainerBattleActorFactory {
    public BattleActor create(Trainer trainer, ServerPlayerEntity player) {
        return new TrainerBattleActor(
                trainer.name,
                UUID.randomUUID(),
                trainer.pokemons.stream().map(SafeCopyBattlePokemonFactory::create).toList(),
                new Generation5AI(),
                player
        );
    }
}
