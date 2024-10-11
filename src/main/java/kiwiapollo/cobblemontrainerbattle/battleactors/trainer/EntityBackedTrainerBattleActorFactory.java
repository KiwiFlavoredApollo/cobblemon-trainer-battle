package kiwiapollo.cobblemontrainerbattle.battleactors.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.Generation5AI;
import kiwiapollo.cobblemontrainerbattle.battleactors.SafeCopyBattlePokemonFactory;
import kiwiapollo.cobblemontrainerbattle.common.SmogonPokemon;
import kiwiapollo.cobblemontrainerbattle.entities.TrainerEntity;
import kiwiapollo.cobblemontrainerbattle.exceptions.PokemonParseException;
import kiwiapollo.cobblemontrainerbattle.common.SmogonPokemonParser;
import kiwiapollo.cobblemontrainerbattle.common.Trainer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntityBackedTrainerBattleActorFactory implements TrainerBattleActorFactory {
    private final TrainerEntity trainerEntity;
    private final ServerPlayerEntity player;

    public EntityBackedTrainerBattleActorFactory(ServerPlayerEntity player, TrainerEntity trainerEntity) {
        this.player = player;
        this.trainerEntity = trainerEntity;
    }

    @Override
    public BattleActor createWithStatusQuo(Identifier identifier) {
        Trainer trainer = CobblemonTrainerBattle.trainers.get(identifier);
        SmogonPokemonParser parser = new SmogonPokemonParser(player);

        List<Pokemon> pokemons = new ArrayList<>();
        for (SmogonPokemon smogonPokemon : trainer.pokemons) {
            try {
                pokemons.add(parser.toPokemon(smogonPokemon));

            } catch (PokemonParseException ignored) {

            }
        }

        List<BattlePokemon> battlePokemons = pokemons.stream()
                .map(SafeCopyBattlePokemonFactory::create).toList();

        return new EntityBackedTrainerBattleActor(
                identifier.getPath(),
                UUID.randomUUID(),
                battlePokemons,
                new Generation5AI(),
                trainerEntity
        );
    }

    @Override
    public BattleActor createWithFlatLevelFullHealth(Identifier identifier, int level) {
        Trainer trainer = CobblemonTrainerBattle.trainers.get(identifier);
        SmogonPokemonParser parser = new SmogonPokemonParser(player);

        List<Pokemon> pokemons = new ArrayList<>();
        for (SmogonPokemon smogonPokemon : trainer.pokemons) {
            try {
                pokemons.add(parser.toPokemon(smogonPokemon));

            } catch (PokemonParseException ignored) {

            }
        }

        pokemons.forEach(Pokemon::heal);
        pokemons.forEach(pokemon -> pokemon.setLevel(level));

        List<BattlePokemon> battlePokemons = pokemons.stream()
                .map(SafeCopyBattlePokemonFactory::create).toList();
        String trainerName = Paths.get(identifier.getPath())
                .getFileName().toString().replace(".json", "");

        return new EntityBackedTrainerBattleActor(
                trainerName,
                UUID.randomUUID(),
                battlePokemons,
                new Generation5AI(),
                trainerEntity
        );
    }
}
