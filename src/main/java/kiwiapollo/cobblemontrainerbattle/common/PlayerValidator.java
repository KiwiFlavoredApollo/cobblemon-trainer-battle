package kiwiapollo.cobblemontrainerbattle.common;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.exception.*;
import kiwiapollo.cobblemontrainerbattle.exception.battlecondition.MaximumPartyLevelException;
import kiwiapollo.cobblemontrainerbattle.exception.battlecondition.MinimumPartyLevelException;
import kiwiapollo.cobblemontrainerbattle.exception.battlecondition.RematchNotAllowedException;
import kiwiapollo.cobblemontrainerbattle.parser.SmogonPokemonParser;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

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
}
