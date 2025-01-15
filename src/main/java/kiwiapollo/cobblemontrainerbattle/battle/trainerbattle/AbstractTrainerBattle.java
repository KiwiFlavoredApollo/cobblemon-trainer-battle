package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.*;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.parser.history.BattleRecord;
import kiwiapollo.cobblemontrainerbattle.parser.history.PlayerHistoryStorage;
import kotlin.Unit;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class AbstractTrainerBattle implements TrainerBattle {
    private final PlayerBattleParticipant player;
    private final TrainerBattleParticipant trainer;
    private final List<MessagePredicate<PlayerBattleParticipant>> predicates;

    private UUID battleId;

    public AbstractTrainerBattle(
            PlayerBattleParticipant player,
            TrainerBattleParticipant trainer
    ) {
        this.player = player;
        this.trainer = trainer;

        this.predicates = List.of(
                new PlayerNotBusyPredicate.PlayerBattleParticipantPredicate(),
                new PlayerPartyNotEmptyPredicate(),
                new PlayerPartyNotFaintedPredicate(),
                new MinimumPartySizePredicate.PlayerPredicate(trainer.getBattleFormat())
        );
    }

    @Override
    public void start() throws BattleStartException {
        for (MessagePredicate<PlayerBattleParticipant> predicate : predicates) {
            if (!predicate.test(player)) {
                player.sendErrorMessage(predicate.getErrorMessage());
                throw new BattleStartException();
            }
        }

        Cobblemon.INSTANCE.getStorage()
                .getParty(player.getPlayerEntity()).toGappyList().stream()
                .filter(Objects::nonNull)
                .forEach(Pokemon::recall);

        Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                trainer.getBattleFormat(),
                new BattleSide(player.createBattleActor()),
                new BattleSide(trainer.createBattleActor(player.getPlayerEntity())),
                false

        ).ifSuccessful(pokemonBattle -> {
            battleId = pokemonBattle.getBattleId();

            PlayerBattleActor actor = (PlayerBattleActor) pokemonBattle.getActor(player.getPlayerEntity());
            SoundEvent battleTheme = trainer.getBattleTheme().orElse(CobblemonSounds.PVN_BATTLE);
            actor.setBattleTheme(battleTheme);

            player.sendInfoMessage(Text.translatable("command.cobblemontrainerbattle.success.trainerbattle", trainer.getName()));
            CobblemonTrainerBattle.LOGGER.info("Started trainer battle : {} versus {}", player.getName(), trainer.getId());

            return Unit.INSTANCE;
        });
    }

    @Override
    public void onPlayerVictory() {
        trainer.onPlayerVictory(player.getPlayerEntity());
        updateVictoryRecord();
    }

    @Override
    public void onPlayerDefeat() {
        trainer.onPlayerDefeat(player.getPlayerEntity());
        updateDefeatRecord();
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

    private BattleRecord getBattleRecord() {
        return (BattleRecord) PlayerHistoryStorage.getInstance().getOrCreate(getPlayer().getUuid()).getOrCreate(getTrainer().getId());
    }

    private void updateVictoryRecord() {
        getBattleRecord().setVictoryCount(getBattleRecord().getVictoryCount() + 1);
    }

    private void updateDefeatRecord() {
        getBattleRecord().setDefeatCount(getBattleRecord().getDefeatCount() + 1);
    }
}
