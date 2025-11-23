package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.advancement.CustomCriteria;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.predicate.*;
import kiwiapollo.cobblemontrainerbattle.exception.BattleStartException;
import kiwiapollo.cobblemontrainerbattle.global.history.BattleRecord;
import kiwiapollo.cobblemontrainerbattle.global.history.PlayerHistoryStorage;
import kotlin.Unit;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class AbstractTrainerBattle implements TrainerBattle {
    private final PlayerBattleParticipant player;
    private final TrainerBattleParticipant trainer;

    private UUID battleId;

    public AbstractTrainerBattle(
            PlayerBattleParticipant player,
            TrainerBattleParticipant trainer
    ) {
        this.player = player;
        this.trainer = trainer;
    }

    @Override
    public void start() throws BattleStartException {
        for (MessagePredicate<PlayerBattleParticipant> predicate : getPredicates()) {
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

            player.sendInfoMessage(Text.translatable("command.cobblemontrainerbattle.success.trainerbattle", trainer.getName().getString()));
            CobblemonTrainerBattle.LOGGER.info("Started trainer battle : {} versus {}", player.getName().getString(), trainer.getIdentifier());

            return Unit.INSTANCE;
        });
    }

    private List<MessagePredicate<PlayerBattleParticipant>> getPredicates() {
        List<MessagePredicate<PlayerBattleParticipant>> predicates = new ArrayList<>();

        predicates.addAll(List.of(
                new PlayerNotBusyPredicate.PlayerBattleParticipantPredicate(),
                new PlayerPartyNotEmptyPredicate(),
                new PlayerPartyNotFaintedPredicate(),
                new MinimumPartySizePredicate.PlayerPredicate(trainer.getBattleFormat())
        ));
        predicates.addAll(trainer.getPredicates());

        return predicates;
    }

    @Override
    public UUID getBattleId() {
        return battleId;
    }
}
