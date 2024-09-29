package kiwiapollo.trainerbattle.battlebuilders;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.trainerbattle.TrainerBattle;
import kiwiapollo.trainerbattle.battleactors.NameTrainerBattleActorFactory;
import kiwiapollo.trainerbattle.battleactors.PlayerBattleActorFactory;
import kiwiapollo.trainerbattle.battleactors.RandomTrainerBattleActorFactory;
import kiwiapollo.trainerbattle.common.Trainer;
import kiwiapollo.trainerbattle.exceptions.EmptyPlayerPartyException;
import kiwiapollo.trainerbattle.exceptions.FaintPlayerPartyException;
import kotlin.Unit;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Objects;
import java.util.stream.Stream;

public class TrainerBattleBuilder {
    public void build(ServerPlayerEntity player, Trainer trainer) {
        try {
            assertNotEmptyPlayerParty(player);
            assertNotFaintPlayerParty(player);

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new PlayerBattleActorFactory().create(player)),
                    new BattleSide(new NameTrainerBattleActorFactory().create(trainer)),
                    false
            ).ifSuccessful(pokemonBattle -> {
                TrainerBattle.TRAINER_BATTLES.add(pokemonBattle);
                return Unit.INSTANCE;
            });

        } catch (EmptyPlayerPartyException e) {
            TrainerBattle.LOGGER.error("Error occurred while starting trainer battle");
            TrainerBattle.LOGGER.error(String.format("%s has no Pokemon", player.getGameProfile().getName()));

        } catch (FaintPlayerPartyException e) {
            TrainerBattle.LOGGER.error("Error occurred while starting trainer battle");
            TrainerBattle.LOGGER.error(String.format("%s Pokemons are all fainted", player.getGameProfile().getName()));
        }
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
