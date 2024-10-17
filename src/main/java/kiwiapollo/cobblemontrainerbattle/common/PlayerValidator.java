package kiwiapollo.cobblemontrainerbattle.common;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.exception.*;
import kiwiapollo.cobblemontrainerbattle.parser.SmogonPokemonParser;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Objects;

public class PlayerValidator {
    public static void assertPlayerPartyNotEmpty(PartyStore party) throws EmptyPlayerPartyException {
        if (party.toGappyList().stream().allMatch(Objects::isNull)) {
            throw new EmptyPlayerPartyException();
        }
    }

    public static void assertPlayerPartyNotFaint(PartyStore party) throws FaintedPlayerPartyException {
        if (party.toGappyList().stream().filter(Objects::nonNull).allMatch(Pokemon::isFainted)) {
            throw new FaintedPlayerPartyException();
        }
    }

    public static void assertPlayerNotBusyWithPokemonBattle(ServerPlayerEntity player) throws BusyWithPokemonBattleException {
        if (Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(player) != null) {
            throw new BusyWithPokemonBattleException();
        }
    }

    public static void assertPlayerPartyAtOrAboveRelativeLevelThreshold(PartyStore party) throws BelowRelativeLevelThresholdException {
        boolean isAtOrBelowRelativeLevelThreshold = party.toGappyList().stream()
                .filter(Objects::nonNull)
                .map(Pokemon::getLevel)
                .allMatch(level -> level < SmogonPokemonParser.RELATIVE_LEVEL_THRESHOLD);

        if (isAtOrBelowRelativeLevelThreshold) {
            throw new BelowRelativeLevelThresholdException();
        }
    }

    public static void assertSatisfiedBattleCondition(PartyStore party, BattleCondition condition) throws BattleConditionException {
        assertSatisfiedMinimumLevelBattleCondition(party, condition);
        assertSatisfiedMaximumLevelBattleCondition(party, condition);
    }

    private static void assertSatisfiedMaximumLevelBattleCondition(PartyStore party, BattleCondition condition) throws BattleConditionException {
        try {
            boolean isAtOrBelowMaximumPartyLevel = party.toGappyList().stream()
                    .filter(Objects::nonNull)
                    .map(Pokemon::getLevel)
                    .allMatch(level -> level <= condition.maximumPartyLevel);

            if (!isAtOrBelowMaximumPartyLevel) {
                throw new BattleConditionException(
                        BattleConditionType.MAXIMUM_PARTY_LEVEL,
                        condition.maximumPartyLevel
                );
            }

        } catch (NullPointerException | IllegalStateException | UnsupportedOperationException ignored) {

        }
    }

    private static void assertSatisfiedMinimumLevelBattleCondition(PartyStore party, BattleCondition condition) throws BattleConditionException {
        try {
            boolean isAtOrAboveMinimumPartyLevel = party.toGappyList().stream()
                    .filter(Objects::nonNull)
                    .map(Pokemon::getLevel)
                    .allMatch(level -> level >= condition.minimumPartyLevel);

            if (!isAtOrAboveMinimumPartyLevel) {
                throw new BattleConditionException(
                        BattleConditionType.MINIMUM_PARTY_LEVEL,
                        condition.minimumPartyLevel
                );
            }

        } catch (NullPointerException | IllegalStateException | UnsupportedOperationException ignored) {

        }
    }
}
