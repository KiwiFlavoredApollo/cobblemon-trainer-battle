package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleactor.VirtualTrainerBattleActor;
import kiwiapollo.cobblemontrainerbattle.command.TrainerBattleCommand;
import kiwiapollo.cobblemontrainerbattle.common.BattleConditionExceptionMessageFactory;
import kiwiapollo.cobblemontrainerbattle.common.PlayerValidator;
import kiwiapollo.cobblemontrainerbattle.exception.*;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.resulthandler.ResultHandler;
import kotlin.Unit;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.UUID;

public class VirtualTrainerBattle implements TrainerBattle {
    public static final int FLAT_LEVEL = 100;

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

            Cobblemon.INSTANCE.getStorage()
                    .getParty(player.getPlayerEntity()).toGappyList().stream()
                    .filter(Objects::nonNull)
                    .forEach(Pokemon::recall);

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
                CobblemonTrainerBattle.LOGGER.info("Started virtual trainer battle: {} versus {}", player.getName(), trainer.getName());

                return Unit.INSTANCE;
            });

        } catch (EmptyPlayerPartyException e) {
            player.sendErrorMessage(Text.translatable("command.cobblemontrainerbattle.trainerbattle.empty_player_party"));
            throw new BattleStartException();

        } catch (FaintedPlayerPartyException e) {
            player.sendErrorMessage(Text.translatable("command.cobblemontrainerbattle.trainerbattle.fainted_player_party"));
            throw new BattleStartException();

        } catch (BelowRelativeLevelThresholdException e) {
            player.sendErrorMessage(Text.translatable("command.cobblemontrainerbattle.trainerbattle.below_relative_level_threshold"));
            throw new BattleStartException();

        } catch (BusyWithPokemonBattleException e) {
            player.sendErrorMessage(Text.translatable("command.cobblemontrainerbattle.trainerbattle.busy_with_pokemon_battle"));
            throw new BattleStartException();

        } catch (BattleConditionException e) {
            player.sendErrorMessage(new BattleConditionExceptionMessageFactory().create(e));
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
