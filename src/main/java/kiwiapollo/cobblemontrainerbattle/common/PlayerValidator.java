package kiwiapollo.cobblemontrainerbattle.common;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.exception.*;
import kiwiapollo.cobblemontrainerbattle.exception.battlecondition.MaximumPartyLevelException;
import kiwiapollo.cobblemontrainerbattle.exception.battlecondition.MinimumPartyLevelException;
import kiwiapollo.cobblemontrainerbattle.exception.battlecondition.RematchAllowedException;
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

    public static void assertSatisfiedBattleCondition(TrainerBattle trainerBattle) throws BattleStartException {
        try {
            assertSatisfiedIsRematchAllowedAfterVictoryBattleCondition(trainerBattle);
            assertSatisfiedMinimumLevelBattleCondition(trainerBattle);
            assertSatisfiedMaximumLevelBattleCondition(trainerBattle);

        } catch (RematchAllowedException e) {
            PlayerBattleParticipant player = trainerBattle.getPlayer();
            player.sendErrorMessage(Text.translatable("command.cobblemontrainerbattle.condition.is_rematch_allowed_after_victory"));

            throw new BattleStartException();

        } catch (MinimumPartyLevelException e) {
            PlayerBattleParticipant player = trainerBattle.getPlayer();
            player.sendErrorMessage(Text.translatable("command.cobblemontrainerbattle.condition.minimum_party_level", trainerBattle.getTrainer().getBattleCondition().minimumPartyLevel));

            throw new BattleStartException();

        } catch (MaximumPartyLevelException e) {
            PlayerBattleParticipant player = trainerBattle.getPlayer();
            player.sendErrorMessage(Text.translatable("command.cobblemontrainerbattle.condition.maximum_party_level", trainerBattle.getTrainer().getBattleCondition().maximumPartyLevel));

            throw new BattleStartException();
        }
    }

    private static void assertSatisfiedIsRematchAllowedAfterVictoryBattleCondition(TrainerBattle trainerBattle) throws RematchAllowedException {
        try {
            ServerPlayerEntity player = trainerBattle.getPlayer().getPlayerEntity();
            Identifier trainer = trainerBattle.getTrainer().getIdentifier();
            BattleCondition condition = trainerBattle.getTrainer().getBattleCondition();

            boolean isTrainerDefeated = CobblemonTrainerBattle.trainerBattleHistoryRegistry.get(player.getUuid()).isTrainerDefeated(trainer);
            if (!condition.isRematchAllowedAfterVictory && isTrainerDefeated) {
                throw new RematchAllowedException();
            }

        } catch (NullPointerException ignored) {

        }
    }

    private static void assertSatisfiedMaximumLevelBattleCondition(TrainerBattle trainerBattle) throws MinimumPartyLevelException {
        try {
            PartyStore party = trainerBattle.getPlayer().getParty();
            BattleCondition condition = trainerBattle.getTrainer().getBattleCondition();

            boolean isAtOrBelowMaximumPartyLevel = party.toGappyList().stream()
                    .filter(Objects::nonNull)
                    .map(Pokemon::getLevel)
                    .allMatch(level -> level <= condition.maximumPartyLevel);

            if (!isAtOrBelowMaximumPartyLevel) {
                throw new MinimumPartyLevelException();
            }

        } catch (NullPointerException | IllegalStateException | UnsupportedOperationException ignored) {

        }
    }

    private static void assertSatisfiedMinimumLevelBattleCondition(TrainerBattle trainerBattle) throws MaximumPartyLevelException {
        try {
            PartyStore party = trainerBattle.getPlayer().getParty();
            BattleCondition condition = trainerBattle.getTrainer().getBattleCondition();

            boolean isAtOrAboveMinimumPartyLevel = party.toGappyList().stream()
                    .filter(Objects::nonNull)
                    .map(Pokemon::getLevel)
                    .allMatch(level -> level >= condition.minimumPartyLevel);

            if (!isAtOrAboveMinimumPartyLevel) {
                throw new MaximumPartyLevelException();
            }

        } catch (NullPointerException | IllegalStateException | UnsupportedOperationException ignored) {

        }
    }
}
