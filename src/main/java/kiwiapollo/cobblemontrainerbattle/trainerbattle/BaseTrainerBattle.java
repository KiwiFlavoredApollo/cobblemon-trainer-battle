package kiwiapollo.cobblemontrainerbattle.trainerbattle;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.pokemon.Pokemon;
import kiwiapollo.cobblemontrainerbattle.CobblemonTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.exception.*;
import kiwiapollo.cobblemontrainerbattle.parser.ShowdownPokemonParser;
import kiwiapollo.cobblemontrainerbattle.predicates.*;
import kotlin.Unit;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BaseTrainerBattle implements TrainerBattle {
    private static final List<MessagePredicate<PlayerBattleParticipant>> PREDICATES = List.of(
            new PlayerNotBusyPredicate<>(PlayerBattleParticipant::getPlayerEntity),
            new PlayerPartyNotEmptyPredicate(),
            new PlayerPartyNotFaintPredicate(),
            new RelativeLevelPredicate()
    );

    private final PlayerBattleParticipant player;
    private final TrainerBattleParticipant trainer;

    private UUID battleId;

    public BaseTrainerBattle(
            PlayerBattleParticipant player,
            TrainerBattleParticipant trainer
    ) {
        this.player = player;
        this.trainer = trainer;
    }

    @Override
    public void start() throws BattleStartException {
        for (MessagePredicate<PlayerBattleParticipant> predicate : PREDICATES) {
            if (!predicate.test(player)) {
                player.sendErrorMessage(predicate.getMessage());
                throw new BattleStartException();
            }
        }

        Cobblemon.INSTANCE.getStorage()
                .getParty(player.getPlayerEntity()).toGappyList().stream()
                .filter(Objects::nonNull)
                .forEach(Pokemon::recall);

        Cobblemon.INSTANCE.getBattleRegistry().startBattle(
                BattleFormat.Companion.getGEN_9_SINGLES(),
                new BattleSide(player.createBattleActor()),
                new BattleSide(trainer.createBattleActor()),
                false
        ).ifSuccessful(pokemonBattle -> {
            battleId = pokemonBattle.getBattleId();

            player.sendInfoMessage(Text.translatable("command.cobblemontrainerbattle.trainerbattle.success", trainer.getName()));
            CobblemonTrainerBattle.LOGGER.info("Started virtual trainer battle: {} versus {}", player.getName(), trainer.getName());

            return Unit.INSTANCE;
        });
    }

    @Override
    public void onPlayerVictory() {

    }

    @Override
    public void onPlayerDefeat() {

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
