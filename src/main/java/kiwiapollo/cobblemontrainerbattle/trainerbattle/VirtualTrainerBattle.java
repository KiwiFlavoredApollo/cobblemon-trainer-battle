package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleactors.VirtualTrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.commands.TrainerBattleCommand;
import kiwiapollo.cobblemontrainerbattle.common.BattleConditionExceptionMessageFactory;
import kiwiapollo.cobblemontrainerbattle.common.PlayerValidator;
import kiwiapollo.cobblemontrainerbattle.exceptions.*;
import kiwiapollo.cobblemontrainerbattle.battleparticipants.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipants.TrainerBattleParticipant;
import kotlin.Unit;
import net.minecraft.text.Text;

import java.util.UUID;

public class VirtualTrainerBattle implements TrainerBattle {
    private final PlayerBattleParticipant player;
    private final TrainerBattleParticipant trainer;
    private final ResultHandler resultHandler;

    private UUID battleId;

    public VirtualTrainerBattle(
            PlayerBattleParticipant player,
            TrainerBattleParticipant trainer,
            ResultHandler resultHandler
    ) {
        this.player = player;
        this.trainer = trainer;
        this.resultHandler = resultHandler;
    }

    @Override
    public void start() throws BattleStartException {
        try {
            PlayerValidator.assertPlayerPartyNotEmpty(player.getParty());
            PlayerValidator.assertPlayerPartyNotFaint(player.getParty());
            PlayerValidator.assertPlayerPartyAtOrAboveRelativeLevelThreshold(player.getParty());
            PlayerValidator.assertPlayerNotBusyWithPokemonBattle(player.getPlayerEntity());
            PlayerValidator.assertSatisfiedBattleCondition(player.getParty(), trainer.getBattleCondition());

            Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                    BattleFormat.Companion.getGEN_9_SINGLES(),
                    new BattleSide(new PlayerBattleActor(
                            player.getUuid(),
                            player.getBattleTeam()
                    )),
                    new BattleSide(new VirtualTrainerBattleActor(
                            trainer.getName(),
                            trainer.getUuid(),
                            trainer.getBattleTeam(),
                            trainer.getBattleAI(),
                            player.getPlayerEntity()
                    )),
                    false
            ).ifSuccessful(pokemonBattle -> {
                battleId = pokemonBattle.getBattleId();

                player.sendInfoMessage(Text.translatable("command.cobblemontrainerbattle.trainerbattle.success", trainer.getName()));
                CobblemonTrainerBattle.LOGGER.info("{}: {} versus {}", new TrainerBattleCommand().getLiteral(), player.getName(), trainer.getName());

                return Unit.INSTANCE;
            });

        } catch (EmptyPlayerPartyException e) {
            player.sendErrorMessage(Text.translatable("command.cobblemontrainerbattle.common.empty_player_party"));
            CobblemonTrainerBattle.LOGGER.error("Player has no Pokemon: {}", player.getName());
            throw new BattleStartException();

        } catch (FaintedPlayerPartyException e) {
            player.sendErrorMessage(Text.translatable("command.cobblemontrainerbattle.common.fainted_player_party"));
            CobblemonTrainerBattle.LOGGER.error("Pokemons are all fainted: {}", player.getName());
            throw new BattleStartException();

        } catch (BelowRelativeLevelThresholdException e) {
            player.sendErrorMessage(Text.translatable("command.cobblemontrainerbattle.common.below_relative_level_threshold"));
            CobblemonTrainerBattle.LOGGER.error("Pokemon levels are below relative level threshold: {}", player.getName());
            throw new BattleStartException();

        } catch (BusyWithPokemonBattleException e) {
            player.sendErrorMessage(Text.translatable("command.cobblemontrainerbattle.common.busy_with_pokemon_battle"));
            CobblemonTrainerBattle.LOGGER.error("Player is busy with Pokemon battle: {}", player.getName());
            throw new BattleStartException();

        } catch (BattleConditionException e) {
            player.sendErrorMessage(new BattleConditionExceptionMessageFactory().create(e));
            CobblemonTrainerBattle.LOGGER.error("Trainer condition not satisfied: {}, {}", e.getBattleConditionType(), e.getRequiredValue());
            throw new BattleStartException();
        }
    }

    @Override
    public void onPlayerVictory() {
        resultHandler.onVictory();
    }

    @Override
    public void onPlayerDefeat() {
        resultHandler.onDefeat();
    }

    @Override
    public UUID getBattleId() {
        return battleId;
    }

    @Override
    public PlayerBattleParticipant getPlayer() {
        return player;
    }

    @Override
    public TrainerBattleParticipant getTrainer() {
        return trainer;
    }
}
