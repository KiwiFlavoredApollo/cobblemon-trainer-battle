package kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.session;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.player.PlayerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.battle.battleparticipant.trainer.TrainerBattleParticipant;
import kiwiapollo.cobblemontrainerbattle.exception.*;
import kiwiapollo.cobblemontrainerbattle.battle.predicates.MessagePredicate;
import kiwiapollo.cobblemontrainerbattle.battle.session.Session;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.BaseTrainerBattle;
import kiwiapollo.cobblemontrainerbattle.battle.trainerbattle.TrainerBattle;

import java.util.UUID;

public class SessionTrainerBattle implements TrainerBattle {
    private final BaseTrainerBattle battle;
    private final Session session;

    public SessionTrainerBattle(
            PlayerBattleParticipant player,
            TrainerBattleParticipant trainer,
            Session session
    ) {
        this.battle = new BaseTrainerBattle(player, trainer);
        this.session = session;
    }

    @Override
    public void start() throws BattleStartException {
        for (MessagePredicate<PlayerBattleParticipant> predicate : session.getBattlePredicates()) {
            if (!predicate.test(getPlayer())) {
                getPlayer().sendErrorMessage(predicate.getErrorMessage());
                throw new BattleStartException();
            }
        }

        battle.start();
    }

    @Override
    public void onPlayerVictory() {
        getPlayer().onVictory();
        session.onBattleVictory();
    }

    @Override
    public void onPlayerDefeat() {
        getPlayer().onDefeat();
        session.onBattleDefeat();
    }

    @Override
    public UUID getBattleId() {
        return battle.getBattleId();
    }

    @Override
    public PlayerBattleParticipant getPlayer() {
        return battle.getPlayer();
    }

    @Override
    public TrainerBattleParticipant getTrainer() {
        return battle.getTrainer();
    }
}
