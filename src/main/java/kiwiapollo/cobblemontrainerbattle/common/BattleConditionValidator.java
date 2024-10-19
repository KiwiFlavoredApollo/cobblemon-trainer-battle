package kiwiapollo.cobblemontrainerbattle.common;

import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.exception.battlecondition.MaximumPartyLevelException;
import kiwiapollo.cobblemontrainerbattle.exception.battlecondition.MinimumPartyLevelException;
import kiwiapollo.cobblemontrainerbattle.exception.battlecondition.RematchNotAllowedException;
import kiwiapollo.cobblemontrainerbattle.trainerbattle.TrainerBattle;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class BattleConditionValidator {
    public static void assertBattleConditionSatisfied(TrainerBattle trainerBattle) throws BattleStartException {
        try {
            assertIsRematchAllowedAfterVictorySatisfied(trainerBattle);
            assertMinimumPartyLevelSatisfied(trainerBattle);
            assertMaximumPartyLevelSatisfied(trainerBattle);

        } catch (RematchNotAllowedException e) {
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

    private static void assertIsRematchAllowedAfterVictorySatisfied(TrainerBattle trainerBattle) throws RematchNotAllowedException {
        try {
            ServerPlayerEntity player = trainerBattle.getPlayer().getPlayerEntity();
            Identifier trainer = trainerBattle.getTrainer().getIdentifier();
            BattleCondition condition = trainerBattle.getTrainer().getBattleCondition();

            boolean isTrainerDefeated = CobblemonTrainerBattle.playerBattleHistoryRegistry.get(player.getUuid()).isTrainerDefeated(trainer);
            if (!condition.isRematchAllowedAfterVictory && isTrainerDefeated) {
                throw new RematchNotAllowedException();
            }

        } catch (NullPointerException ignored) {

        }
    }

    private static void assertMaximumPartyLevelSatisfied(TrainerBattle trainerBattle) throws MaximumPartyLevelException {
        try {
            PartyStore party = trainerBattle.getPlayer().getParty();
            BattleCondition condition = trainerBattle.getTrainer().getBattleCondition();

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

    private static void assertMinimumPartyLevelSatisfied(TrainerBattle trainerBattle) throws MinimumPartyLevelException {
        try {
            PartyStore party = trainerBattle.getPlayer().getParty();
            BattleCondition condition = trainerBattle.getTrainer().getBattleCondition();

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
