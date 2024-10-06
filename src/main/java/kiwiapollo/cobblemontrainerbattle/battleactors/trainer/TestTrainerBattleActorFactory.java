package kiwiapollo.cobblemontrainerbattle.battleactors.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.battles.actor.TrainerBattleActor;
import com.cobblemon.mod.common.battles.ai.RandomBattleAI;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.common.SafeCopyBattlePokemonFactory;
import kotlin.Unit;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.UUID;

public class TestTrainerBattleActorFactory {
    public BattleActor create(int level) {
        Pokemon pikachu = PokemonSpecies.INSTANCE
                .getByIdentifier(Identifier.of("cobblemon", "pikachu"))
                .create(level);

        return new TrainerBattleActor(
                "MyTrainer",
                UUID.randomUUID(),
                List.of(
                        SafeCopyBattlePokemonFactory.create(pikachu)
                ),
                new RandomBattleAI()
        );
    }
}
