package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.common.TrainerConditionType;
import kiwiapollo.cobblemontrainerbattle.exceptions.*;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Objects;
import java.util.stream.Stream;

public class PlayerValidator {
    private final ServerPlayerEntity player;

    public PlayerValidator(ServerPlayerEntity player) {
        this.player = player;
    }

    public void assertNotEmptyPlayerParty()
            throws EmptyPlayerPartyException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);

        if (playerPartyStore.toGappyList().stream().allMatch(Objects::isNull)) {
            throw new EmptyPlayerPartyException();
        }
    }

    public void assertNotFaintPlayerParty()
            throws FaintedPlayerPartyException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        Stream<Pokemon> pokemons = playerPartyStore.toGappyList().stream().filter(Objects::nonNull);

        if (pokemons.allMatch(Pokemon::isFainted)) {
            throw new FaintedPlayerPartyException();
        }
    }

    public void assertPlayerNotBusyWithPokemonBattle()
            throws BusyWithPokemonBattleException {
        if (Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(player) != null) {
            throw new BusyWithPokemonBattleException();
        }
    }

    public void assertPlayerPartyAtOrAboveRelativeLevelThreshold()
            throws BelowRelativeLevelThresholdException {
        PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
        Stream<Pokemon> pokemons = playerPartyStore.toGappyList().stream().filter(Objects::nonNull);

        if (pokemons.map(Pokemon::getLevel).allMatch(level -> level < TrainerFileParser.RELATIVE_LEVEL_THRESHOLD)) {
            throw new BelowRelativeLevelThresholdException();
        }
    }

    public void assertSatisfiedTrainerCondition(Trainer trainer)
            throws UnsatisfiedTrainerConditionException {
        assertSatisfiedMinimumLevelTrainerCondition(trainer);
        assertSatisfiedMaximumLevelTrainerCondition(trainer);
    }

    private void assertSatisfiedMaximumLevelTrainerCondition(Trainer trainer)
            throws UnsatisfiedTrainerConditionException {
        try {
            PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
            int maximumPartyLevel = CobblemonTrainerBattle.trainerFiles
                    .get(trainer.name).configuration
                    .get("condition").getAsJsonObject()
                    .get("maximumPartyLevel").getAsInt();
            boolean isAtOrBelowPartyMaximumLevel = playerPartyStore.toGappyList().stream()
                    .filter(Objects::nonNull)
                    .map(Pokemon::getLevel)
                    .allMatch(level -> level <= maximumPartyLevel);

            if (!isAtOrBelowPartyMaximumLevel) {
                throw new UnsatisfiedTrainerConditionException(
                        String.format("Player did not satisfy maximum level condition: %s, %s", player, trainer.name),
                        TrainerConditionType.MAXIMUM_PARTY_LEVEL,
                        maximumPartyLevel);
            }

        } catch (NullPointerException | IllegalStateException | UnsupportedOperationException ignored) {

        }
    }

    private void assertSatisfiedMinimumLevelTrainerCondition(Trainer trainer)
            throws UnsatisfiedTrainerConditionException {
        try {
            PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(player);
            int minimumPartyLevel = CobblemonTrainerBattle.trainerFiles
                    .get(trainer.name).configuration
                    .get("condition").getAsJsonObject()
                    .get("minimumPartyLevel").getAsInt();
            boolean isAtOrAbovePartyMinimumLevel = playerPartyStore.toGappyList().stream()
                    .filter(Objects::nonNull)
                    .map(Pokemon::getLevel)
                    .allMatch(level -> level >= minimumPartyLevel);

            if (!isAtOrAbovePartyMinimumLevel) {
                throw new UnsatisfiedTrainerConditionException(
                        String.format("Player did not satisfy minimum level condition: %s, %s", player, trainer.name),
                        TrainerConditionType.MINIMUM_PARTY_LEVEL,
                        minimumPartyLevel);
            }

        } catch (NullPointerException | IllegalStateException | UnsupportedOperationException ignored) {

        }
    }
}
