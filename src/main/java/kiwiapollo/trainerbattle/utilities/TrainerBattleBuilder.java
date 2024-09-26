package kiwiapollo.trainerbattle.utilities;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.actor.TrainerBattleActor;
import com.cobblemon.mod.common.battles.ai.RandomBattleAI;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.trainerbattle.TrainerBattle;
import kiwiapollo.trainerbattle.exceptions.EmptyPlayerPartyException;
import kiwiapollo.trainerbattle.exceptions.FaintPlayerPartyException;
import kotlin.Unit;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class TrainerBattleBuilder {
    public void build(ServerPlayerEntity player) {
        try {
            assertNotEmptyPlayerParty(player);
            assertNotFaintPlayerParty(player);

            PlayerPartyStore playerPartyStore =
                    Cobblemon.INSTANCE.getStorage().getParty(player);
            PlayerBattleActor playerBattleActor = new PlayerBattleActor(
                    player.getUuid(),
                    getPlayerPartyBattleTeam(playerPartyStore)
            );

            TrainerBattleActor trainerBattleActor = new TrainerBattleActor(
                    "MyTrainer",
                    UUID.randomUUID(),
                    getTrainerPartyBattleTeam(),
                    new RandomBattleAI()
            );

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(playerBattleActor),
                    new BattleSide(trainerBattleActor),
                    false
            ).ifSuccessful(pokemonBattle -> {
                TrainerBattle.TRAINER_BATTLES.add(pokemonBattle);
                return Unit.INSTANCE;
            });

        } catch (EmptyPlayerPartyException e) {
            TrainerBattle.LOGGER.error("%s has no Pokemon");

        } catch (FaintPlayerPartyException e) {
            TrainerBattle.LOGGER.error("%s Pokemons are all fainted");
        }
    }

    private List<? extends BattlePokemon> getTrainerPartyBattleTeam() {
        Pokemon pikachu = PokemonSpecies.INSTANCE.getByIdentifier(
                Identifier.of("cobblemon", "pikachu")
        ).create(20);

        return List.of(
                new BattlePokemon(pikachu, pikachu, pokemonEntity -> Unit.INSTANCE)
        );
    }

    private List<? extends BattlePokemon> getPlayerPartyBattleTeam(PlayerPartyStore playerPartyStore) {
        Stream<Pokemon> pokemons = playerPartyStore.toGappyList().stream()
                .filter(Objects::nonNull)
                .filter(pokemon -> !pokemon.isFainted());

        List<BattlePokemon> playerPartyBattleTeam = playerPartyStore.toBattleTeam(
                false,
                false,
                pokemons.findFirst().get().getUuid()
        );

        return playerPartyBattleTeam;
    }

    private void assertNotEmptyPlayerParty(ServerPlayerEntity player) throws EmptyPlayerPartyException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        if (playerPartyStore.toGappyList().isEmpty()) {
            throw new EmptyPlayerPartyException();
        }
    }

    private void assertNotFaintPlayerParty(ServerPlayerEntity player) throws FaintPlayerPartyException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        Stream<Pokemon> pokemons = playerPartyStore.toGappyList().stream().filter(Objects::nonNull);
        if (pokemons.allMatch(Pokemon::isFainted)) {
            throw new FaintPlayerPartyException();
        }
    }
}
