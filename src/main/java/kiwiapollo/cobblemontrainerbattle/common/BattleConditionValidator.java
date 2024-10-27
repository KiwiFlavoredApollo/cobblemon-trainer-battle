package kiwiapollo.cobblemontrainerbattle.common;

import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.exception.battlecondition.MaximumPartyLevelException;
import kiwiapollo.cobblemontrainerbattle.exception.battlecondition.MinimumPartyLevelException;
import kiwiapollo.cobblemontrainerbattle.exception.battlecondition.RematchNotAllowedException;
import kiwiapollo.cobblemontrainerbattle.parser.PlayerHistory;
import kiwiapollo.cobblemontrainerbattle.parser.PlayerHistoryRegistry;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class BattleConditionValidator {
    public static void assertAllBattleConditionMet(TrainerBattle trainerBattle) throws BattleStartException {
        try {
            PlayerBattleParticipant player = trainerBattle.getPlayer();
            TrainerBattleParticipant trainer = trainerBattle.getTrainer();
            assertRematchAllowedAfterVictory(player.getPlayerEntity(), trainer.getIdentifier(), trainer.getBattleCondition());

            assertAtOrAboveMinimumPartyLevel(player.getParty(), trainer.getBattleCondition());
            assertAtOrBelowMaximumPartyLevel(player.getParty(), trainer.getBattleCondition());

        } catch (RematchNotAllowedException e) {
            PlayerBattleParticipant player = trainerBattle.getPlayer();
            player.sendErrorMessage(Text.translatable("command.cobblemontrainerbattle.condition.is_rematch_allowed_after_victory.trainerbattle"));

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

    public static void assertRematchAllowedAfterVictory(ServerPlayerEntity player, Identifier opponent, BattleCondition condition) throws RematchNotAllowedException {
        try {
            PlayerHistory history = PlayerHistoryRegistry.get(player.getUuid());
            if (!condition.isRematchAllowedAfterVictory && history.isOpponentDefeated(opponent)) {
                throw new RematchNotAllowedException();
            }

        } catch (NullPointerException ignored) {

        }
    }

    public static void assertAtOrBelowMaximumPartyLevel(PartyStore party, BattleCondition condition) throws MaximumPartyLevelException {
        try {
            boolean isAtOrBelowMaximumPartyLevel = party.toGappyList().stream()
                    .filter(Objects::nonNull)
                    .map(Pokemon::getLevel)
                    .allMatch(level -> level <= condition.maximumPartyLevel);

            if (!isAtOrBelowMaximumPartyLevel) {
                throw new MaximumPartyLevelException();
            }

        } catch (NullPointerException | IllegalStateException | UnsupportedOperationException ignored) {

        }
    }

    public static void assertAtOrAboveMinimumPartyLevel(PartyStore party, BattleCondition condition) throws MinimumPartyLevelException {
        try {
            boolean isAtOrAboveMinimumPartyLevel = party.toGappyList().stream()
                    .filter(Objects::nonNull)
                    .map(Pokemon::getLevel)
                    .allMatch(level -> level >= condition.minimumPartyLevel);

            if (!isAtOrAboveMinimumPartyLevel) {
                throw new MinimumPartyLevelException();
            }

        } catch (NullPointerException | IllegalStateException | UnsupportedOperationException ignored) {

        }
    }
}
