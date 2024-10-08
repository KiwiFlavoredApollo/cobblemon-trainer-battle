package kiwiapollo.cobblemontrainerbattle.battleactors.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.common.Generation5AI;
import kiwiapollo.cobblemontrainerbattle.battleactors.SafeCopyBattlePokemonFactory;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.Trainer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.UUID;

public class VirtualTrainerBattleActorFactory implements TrainerBattleActorFactory {
    private final ServerPlayerEntity player;

    public VirtualTrainerBattleActorFactory(ServerPlayerEntity player) {
        this.player = player;
    }

    @Override
    public BattleActor createWithStatusQuo(Trainer trainer) {
        List<BattlePokemon> pokemons = trainer.pokemons.stream()
                .map(SafeCopyBattlePokemonFactory::create).toList();

        return new VirtualTrainerBattleActor(
                trainer.name,
                UUID.randomUUID(),
                pokemons,
                new Generation5AI(),
                player
        );
    }

    @Override
    public BattleActor createWithFlatLevelFullHealth(Trainer trainer, int level) {
        trainer.pokemons.forEach(Pokemon::heal);
        trainer.pokemons.forEach(pokemon -> pokemon.setLevel(level));

        List<BattlePokemon> pokemons = trainer.pokemons.stream()
                .map(SafeCopyBattlePokemonFactory::create).toList();

        return new VirtualTrainerBattleActor(
                trainer.name,
                UUID.randomUUID(),
                pokemons,
                new Generation5AI(),
                player
        );
    }
}
