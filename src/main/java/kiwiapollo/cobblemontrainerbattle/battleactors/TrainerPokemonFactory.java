package kiwiapollo.cobblemontrainerbattle.battleactors;

import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.Trainer;
import kiwiapollo.cobblemontrainerbattle.exceptions.PokemonParseException;
import kiwiapollo.cobblemontrainerbattle.parsers.SmogonPokemon;
import kiwiapollo.cobblemontrainerbattle.parsers.SmogonPokemonParser;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class TrainerPokemonFactory {
    private final ServerPlayerEntity player;

    public TrainerPokemonFactory(ServerPlayerEntity player) {
        this.player = player;
    }

    public List<Pokemon> create(Identifier identifier) {
        Trainer trainer = CobblemonTrainerBattle.trainers.get(identifier);
        SmogonPokemonParser parser = new SmogonPokemonParser(player);

        List<Pokemon> pokemons = new ArrayList<>();
        for (SmogonPokemon smogonPokemon : trainer.pokemons) {
            try {
                pokemons.add(parser.toCobblemonPokemon(smogonPokemon));

            } catch (PokemonParseException ignored) {

            }
        }

        return pokemons;
    }
}
