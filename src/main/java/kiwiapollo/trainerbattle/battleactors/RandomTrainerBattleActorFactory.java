package kiwiapollo.trainerbattle.battleactors;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.battles.actor.TrainerBattleActor;
import com.cobblemon.mod.common.battles.ai.RandomBattleAI;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kotlin.Unit;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.UUID;

public class RandomTrainerBattleActorFactory {
    public BattleActor create(int level) {
        Pokemon pikachu = PokemonSpecies.INSTANCE
                .getByIdentifier(Identifier.of("cobblemon", "pikachu"))
                .create(level);

        return new TrainerBattleActor(
                "MyTrainer",
                UUID.randomUUID(),
                List.of(
                        new BattlePokemon(
                                pikachu,
                                pikachu,
                                pokemonEntity -> {
                                    pokemonEntity.discard();
                                    return Unit.INSTANCE;
                                }
                        )
                ),
                new RandomBattleAI()
        );
    }
}
