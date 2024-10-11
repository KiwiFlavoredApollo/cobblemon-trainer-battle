package kiwiapollo.cobblemontrainerbattle.battleactors.trainer;

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battlefactory.BattleFactory;
import kiwiapollo.cobblemontrainerbattle.common.Generation5AI;
import kiwiapollo.cobblemontrainerbattle.battleactors.SafeCopyBattlePokemonFactory;
import kiwiapollo.cobblemontrainerbattle.parsers.SmogonPokemon;
import kiwiapollo.cobblemontrainerbattle.exceptions.PokemonParseException;
import kiwiapollo.cobblemontrainerbattle.parsers.SmogonPokemonParser;
import kiwiapollo.cobblemontrainerbattle.common.Trainer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VirtualTrainerBattleActorFactory implements TrainerBattleActorFactory {
    private final ServerPlayerEntity player;

    public VirtualTrainerBattleActorFactory(ServerPlayerEntity player) {
        this.player = player;
    }

    @Override
    public BattleActor createWithStatusQuo(Identifier identifier) {
        Trainer trainer = CobblemonTrainerBattle.trainers.get(identifier);
        SmogonPokemonParser parser = new SmogonPokemonParser(player);

        List<Pokemon> pokemons = new ArrayList<>();
        for (SmogonPokemon smogonPokemon : trainer.pokemons) {
            try {
                pokemons.add(parser.toCobblemonPokemon(smogonPokemon));

            } catch (PokemonParseException ignored) {

            }
        }

        List<BattlePokemon> battlePokemons = pokemons.stream()
                .map(SafeCopyBattlePokemonFactory::create).toList();

        return new VirtualTrainerBattleActor(
                identifier.getPath(),
                UUID.randomUUID(),
                battlePokemons,
                new Generation5AI(),
                player
        );
    }

    @Override
    public BattleActor createWithFlatLevelFullHealth(Identifier identifier, int level) {
        Trainer trainer = CobblemonTrainerBattle.trainers.get(identifier);
        SmogonPokemonParser parser = new SmogonPokemonParser(player);

        List<Pokemon> pokemons = new ArrayList<>();
        for (SmogonPokemon smogonPokemon : trainer.pokemons) {
            try {
                pokemons.add(parser.toCobblemonPokemon(smogonPokemon));

            } catch (PokemonParseException ignored) {

            }
        }

        pokemons.forEach(Pokemon::heal);
        pokemons.forEach(pokemon -> pokemon.setLevel(level));

        List<BattlePokemon> battlePokemons = pokemons.stream()
                .map(SafeCopyBattlePokemonFactory::create).toList();

        return new VirtualTrainerBattleActor(
                identifier.getPath(),
                UUID.randomUUID(),
                battlePokemons,
                new Generation5AI(),
                player
        );
    }

    public BattleActor createForBattleFactory(Identifier identifier, int level) {
        Trainer trainer = CobblemonTrainerBattle.trainers.get(identifier);
        SmogonPokemonParser parser = new SmogonPokemonParser(player);

        List<Pokemon> pokemons = new ArrayList<>();
        for (SmogonPokemon smogonPokemon : trainer.pokemons) {
            try {
                pokemons.add(parser.toCobblemonPokemon(smogonPokemon));

            } catch (PokemonParseException ignored) {

            }
        }

        pokemons = pokemons.subList(0, BattleFactory.POKEMON_COUNT);

        pokemons.forEach(Pokemon::heal);
        pokemons.forEach(pokemon -> pokemon.setLevel(level));

        List<BattlePokemon> battlePokemons = pokemons.stream()
                .map(SafeCopyBattlePokemonFactory::create).toList();

        return new VirtualTrainerBattleActor(
                identifier.getPath(),
                UUID.randomUUID(),
                battlePokemons,
                new Generation5AI(),
                player
        );
    }
}
